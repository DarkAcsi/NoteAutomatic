package com.example.noteautomatic.screens.projectCreation

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
            _project.postValue(SuccessResult(projectsRepository.getById(id)))
            imagesRepository.loadImages(id)
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

    fun saveImages(listImage: List<Image>) {
        imagesRepository.localUpdate(listImage)
    }

    override fun deleteImage(image: Image) {
        _project.value?.map {
            viewModelScope.launch {
                _project.postValue(
                    SuccessResult(it.copy(play = imagesRepository.deleteImage(image.id) != 0))
                )
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