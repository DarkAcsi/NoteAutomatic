package com.example.noteautomatic.screens.projectRun

import androidx.lifecycle.viewModelScope
import com.example.noteautomatic.foundation.base.BaseViewModel
import com.example.noteautomatic.foundation.model.project.ProjectsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectRunViewModel(
    private val projectsRepository: ProjectsRepository
) : BaseViewModel() {

//    private val _projectRun = MutableLiveResult<Project>()
//    val projectRun: LiveResult<Project> = _projectRun

    fun loadProject(projectId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
//                _projectRun.value = projectsRepository.getById(projectId)
            }
        }
    }


}