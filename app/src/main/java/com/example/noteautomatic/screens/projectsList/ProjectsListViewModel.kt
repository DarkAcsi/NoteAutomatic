package com.example.noteautomatic.screens.projectsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteautomatic.repositories.ProjectsListener
import com.example.noteautomatic.repositories.ProjectsRepository

class ProjectsListViewModel(
    private val projectsService: ProjectsRepository
) : ViewModel() {

    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> = _projects


    private val listener: ProjectsListener = {
        _projects.value = it
    }

    init {
        loadProjects()
    }

    override fun onCleared() {
        super.onCleared()
        projectsService.removeListener(listener)
    }

    fun loadProjects() {
        projectsService.addListener(listener)
    }

    fun settingProject(project: Project) {
        // todo?(settingProject)
    }

    fun deleteProject(project: Project) {
        projectsService.deleteProject(project)
    }

    fun selectProject(project: Project) {
        // todo(selectProject)
    }

}