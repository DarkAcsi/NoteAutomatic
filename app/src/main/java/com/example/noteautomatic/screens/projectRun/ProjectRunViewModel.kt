package com.example.noteautomatic.screens.projectRun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.noteautomatic.database.classes.FullProject
import com.example.noteautomatic.foundation.base.BaseViewModel
import com.example.noteautomatic.model.project.ProjectsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectRunViewModel(
    private val projectsRepository: ProjectsRepository
) : BaseViewModel() {

    private val _projectRun = MutableLiveData<FullProject>()
    val projectRun: LiveData<FullProject> = _projectRun

    fun loadProject(projectId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _projectRun.value = projectsRepository.getById(projectId)
            }
        }
    }

    fun deleteProject() {
        val projectRun = this.projectRun.value ?: return
        projectsRepository.deleteProject(projectRun.id)
    }

}