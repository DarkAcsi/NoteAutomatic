package com.example.noteautomatic.screens.projectCreation

import androidx.lifecycle.viewModelScope
import com.example.noteautomatic.foundation.base.BaseViewModel
import com.example.noteautomatic.foundation.base.LiveResult
import com.example.noteautomatic.foundation.base.MediatorLiveResult
import com.example.noteautomatic.foundation.base.MutableLiveResult
import com.example.noteautomatic.foundation.base.SuccessResult
import com.example.noteautomatic.foundation.classes.FullProject
import com.example.noteautomatic.foundation.database.entities.ProjectEntity
import com.example.noteautomatic.foundation.model.project.ProjectsRepository
import kotlinx.coroutines.launch

class ProjectCreationViewModel(
    private val projectsRepository: ProjectsRepository
) : BaseViewModel() {

    private val _viewState = MutableLiveResult<ViewState>()
    val viewState: LiveResult<ViewState> = _viewState

    fun loadProject(id: Long) {
        viewModelScope.launch {
            _viewState.postValue(SuccessResult(ViewState(projectsRepository.getById(id))))
        }
    }


    fun save(nameEd: String, nameTv: String, speed: Int, project: FullProject) {
        viewModelScope.launch {
            if ((nameEd.isEmpty()) and (project.id == 0L)) {

            }
            val availableName = projectsRepository.getNames(nameEd, project.id)
            if (!availableName) {

            }
//            var projectId = 0L
//            if (availableName) {
//                projectId = projectsRepository.updateProject(
//                    ProjectEntity.toProjectEntity(
//                        project,
//                        nameEd
//                    )
//                ).id
//            }
        }
    }

    data class ViewState(
        val fullProject: FullProject?,
        val isSaving: Boolean = false,
    )
}