package com.example.noteautomatic.screens.projectCreation

import android.app.Activity
import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.noteautomatic.foundation.base.BaseViewModel
import com.example.noteautomatic.foundation.base.LiveResult
import com.example.noteautomatic.foundation.base.MediatorLiveResult
import com.example.noteautomatic.foundation.base.MutableLiveResult
import com.example.noteautomatic.foundation.base.PendingResult
import com.example.noteautomatic.foundation.base.SuccessResult
import com.example.noteautomatic.foundation.database.entities.Image
import com.example.noteautomatic.foundation.database.entities.Project
import com.example.noteautomatic.foundation.model.image.ImagesListener
import com.example.noteautomatic.foundation.model.image.ImagesRepository
import com.example.noteautomatic.foundation.model.project.ProjectsRepository
import kotlinx.coroutines.launch

class ProjectCreationViewModel(
    private val projectsRepository: ProjectsRepository,
    private val imagesRepository: ImagesRepository,
) : BaseViewModel(), ImageActionListener {

    private val _viewState = MediatorLiveResult<ViewState>(PendingResult())
    val viewState: LiveResult<ViewState> = _viewState

    private val _project = MutableLiveResult<Project>(PendingResult())
    private val _listImage = MutableLiveData<List<Image>>()
    private val _isSaving = MutableLiveData(false)

    private val listener: ImagesListener = {
        _listImage.postValue(it)
    }

    init {
        imagesRepository.addListener(listener)
        _viewState.addSource(_project) { mergeSources() }
        _viewState.addSource(_listImage) { mergeSources() }
        _viewState.addSource(_isSaving) { mergeSources() }
    }

    fun loadProject(id: Long) {
        viewModelScope.launch {
            val listImage = imagesRepository.loadImages(id)
            val fullProject = projectsRepository.getById(id)
            _listImage.postValue(listImage)
            _project.postValue(SuccessResult(fullProject))
        }
    }

    fun save(
        nameEd: String,
        speed: Int,
        project: Project
    ) {
        viewModelScope.launch {
            _isSaving.postValue(true)
            if ((project.id == 0L) and (nameEd.isEmpty())) {
                _project.postValue(SuccessResult(project.copy(name = "This field is required")))
            } else if ((project.id == 0L) and (!projectsRepository.getNames(nameEd, project.id))) {
                _project.postValue(SuccessResult(project.copy(name = "Name is already used")))
            } else {
                var fullProject =
                    if (projectsRepository.getNames(nameEd, project.id) and nameEd.isNotBlank())
                        Project(project.id, nameEd, speed, _listImage.value?.isNotEmpty() == true)
                    else Project(
                        project.id,
                        project.name,
                        speed,
                        _listImage.value?.isNotEmpty() == true
                    )
                fullProject = projectsRepository.updateProject(fullProject)
                _project.postValue(SuccessResult(fullProject))

                imagesRepository.saveImages(fullProject.id)
            }
            _isSaving.postValue(false)
        }
    }

    private fun saveImages(listImage: List<Image>, project: Project) {
        if (project.id == 0L) imagesRepository.localUpdate(listImage)
        else viewModelScope.launch {
            _isSaving.postValue(true)
            imagesRepository.localUpdate(listImage)
            imagesRepository.saveImages(project.id)
            var fullProject = Project(
                project.id,
                project.name,
                project.speed,
                _listImage.value?.isNotEmpty() == true
            )
            fullProject = projectsRepository.updateProject(fullProject)
            _project.postValue(SuccessResult(fullProject))
            _isSaving.postValue(false)
        }
    }

    fun deleteProject(id: Long, toMenu: () -> Unit) {
        viewModelScope.launch {
            _project.postValue(PendingResult())
            projectsRepository.deleteProject(id)
            toMenu()
        }
    }

    fun runProject(save: () -> Unit, toMenu: () -> Unit) {
        viewModelScope.launch {
            _isSaving.postValue(true)
            save()
            _isSaving.postValue(false)
            toMenu()
        }
    }

    fun createImagePicker(result: ActivityResult, newProject: Project) {
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val imagesUri = mutableListOf<Uri>()
            val images = mutableListOf<Image>()

            data?.clipData?.let { clipData ->
                for (i in 0 until clipData.itemCount) {
                    imagesUri.add(clipData.getItemAt(i).uri)
                    images.add(Image(0, 0, 0, clipData.getItemAt(i).uri, -1))
                }
            } ?: data?.data?.let { uri ->
                imagesUri.add(uri)
                images.add(Image(0, 0, 0, uri, -1))
            }
            viewModelScope.launch {
                _isSaving.postValue(true)
                saveImages(images, newProject)
                _isSaving.postValue(false)
            }
        }
    }

    fun createPdfPicker(result: ActivityResult, newProject: Project, context: Context) {
        if (result.resultCode == Activity.RESULT_OK) {
            var file = emptyList<Image>()
            result.data?.data?.let { uri ->
                val pageCount = getPageCount(context, uri)
                file = listOf(Image(0, 0, 0, uri, pageCount))
            }
            viewModelScope.launch {
                _isSaving.postValue(true)
                saveImages(file, newProject)
                _isSaving.postValue(false)
            }
        }
    }

    private fun getPageCount(context: Context, uri: Uri): Int {
        var pageCount = 0
        context.contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
            PdfRenderer(pfd).use { pdfRenderer ->
                pageCount = pdfRenderer.pageCount
            }
        }
        return pageCount
    }

    override fun deleteImage(image: Image) {
        _project.value?.map {
            viewModelScope.launch {
                val play = imagesRepository.deleteImage(image) != 0
                if (it.play != play) {
                    _isSaving.postValue(true)
                    projectsRepository.updateProject(it.copy(play = play))
                    _project.postValue(
                        SuccessResult(it.copy(play = play))
                    )
                    _isSaving.postValue(false)
                }
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        imagesRepository.moveImage(fromPosition, toPosition)
    }

    private fun mergeSources() {
        val project = _project.value ?: return
        val listImage = _listImage.value ?: return
        val isSaving = _isSaving.value ?: return
        _viewState.value = project.map {
            ViewState(
                project = it,
                listImage = listImage,
                isSaving = isSaving
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        imagesRepository.removeListener(listener)
    }

    fun tryAgain() {
        viewState.value?.let { result ->
            result.map {
                loadProject(it.project.id)
            }
        }
    }

    data class ViewState(
        val project: Project,
        val listImage: List<Image>,
        val isSaving: Boolean,
    )
}