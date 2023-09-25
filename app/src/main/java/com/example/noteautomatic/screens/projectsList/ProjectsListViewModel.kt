package com.example.noteautomatic.screens.projectsList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteautomatic.repositories.ProjectsListener
import com.example.noteautomatic.repositories.ProjectsRepository

class ProjectsListViewModel(
    private val projectsRepository: ProjectsRepository
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
        projectsRepository.removeListener(listener)
    }

    fun loadProjects() {
        projectsRepository.addListener(listener)
    }

    fun deleteProjects() {
        projectsRepository.deleteProjects()
    }

    fun selectAllProjects(selected: Boolean?) {
        projectsRepository.selectAllProjects(selected)
    }

    fun selectProjects(project: Project, selected: Boolean) {
        projectsRepository.selectProjects(project, selected)
    }

    fun selectMoreProject(project: Project) : Boolean {
        return projectsRepository.selectMoreProjects(project)
    }

}