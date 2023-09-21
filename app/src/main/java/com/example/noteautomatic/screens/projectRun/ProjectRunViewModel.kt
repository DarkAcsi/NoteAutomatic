package com.example.noteautomatic.screens.projectRun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteautomatic.ProjectNotFoundException
import com.example.noteautomatic.screens.projectsList.projects.FullProject
import com.example.noteautomatic.screens.projectsList.projects.ProjectsService

class ProjectRunViewModel(
    private val projectsService: ProjectsService
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