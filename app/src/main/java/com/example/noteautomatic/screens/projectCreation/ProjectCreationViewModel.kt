package com.example.noteautomatic.screens.projectCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteautomatic.screens.projectsList.FullProject
import com.example.noteautomatic.repositories.ProjectNotFoundException
import com.example.noteautomatic.repositories.ProjectsRepository

class ProjectCreationViewModel(
    private val projectsService: ProjectsRepository
) : ViewModel() {

    private val _projectRun = MutableLiveData<FullProject>()
    val projectRun: LiveData<FullProject> = _projectRun

    fun loadProject(projectId: Long) {
        try {
            _projectRun.value = projectsService.getById(projectId)
        } catch (e: ProjectNotFoundException) {
            e.printStackTrace()
        }
    }

    fun deleteProject() {
        val projectRun = this.projectRun.value ?: return
        projectsService.deleteProject(projectRun.project)
    }
}