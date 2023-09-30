package com.example.noteautomatic.screens.projectCreation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.noteautomatic.foundation.base.BaseViewModel
import com.example.noteautomatic.foundation.base.LiveResult
import com.example.noteautomatic.foundation.base.MediatorLiveResult
import com.example.noteautomatic.foundation.base.MutableLiveResult
import com.example.noteautomatic.foundation.base.SuccessResult
import com.example.noteautomatic.foundation.classes.FullProject
import com.example.noteautomatic.foundation.classes.Image
import com.example.noteautomatic.foundation.database.entities.ProjectEntity
import com.example.noteautomatic.foundation.model.project.ProjectsRepository
import kotlinx.coroutines.launch

class ProjectCreationViewModel(
    private val projectsRepository: ProjectsRepository
) : BaseViewModel() {

    private val _viewState = MediatorLiveResult<ViewState>()
    val viewState: LiveResult<ViewState> = _viewState

    private val _fullProject = MutableLiveResult<FullProject>()
    private val _isSaving = MutableLiveData(false)

    fun loadProject(id: Long) {
        viewModelScope.launch {
            _fullProject.postValue(SuccessResult(projectsRepository.getById(id)))
        }
        _viewState.addSource(_fullProject) { mergeSources() }
        _viewState.addSource(_isSaving) { mergeSources() }
    }


    fun save(
        nameEd: String,
        speed: Int,
        project: FullProject,
        listImage: List<Image> = emptyList()
    ) {
        viewModelScope.launch {
            _isSaving.postValue(true)
            if ((project.id == 0L) and (nameEd.isEmpty())) {
                _fullProject.postValue(SuccessResult(project.copy(name = "This field is required")))
            } else if ((project.id == 0L) and (!projectsRepository.getNames(nameEd, project.id))) {
                _fullProject.postValue(SuccessResult(project.copy(name = "Name is already used")))
            } else {
                var fullProject = if (projectsRepository.getNames(nameEd, project.id))
                    FullProject(project.id, nameEd, speed)
                else FullProject(project.id, project.name, speed)
                fullProject =
                    projectsRepository.updateProject(ProjectEntity.toProjectEntity(fullProject))
                fullProject = fullProject.copy(listImage = listImage)
                _fullProject.postValue(SuccessResult(fullProject))
            }
            _isSaving.postValue(false)
        }
    }

    private fun mergeSources() {
        val project = _fullProject.value ?: return
        val isSaving = _isSaving.value ?: return
        _viewState.value = project.map {
            ViewState(
                fullProject = it,
                isSaving = isSaving
            )
        }
    }

    data class ViewState(
        val fullProject: FullProject,
        val isSaving: Boolean,
    )
}