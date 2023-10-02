package com.example.noteautomatic.screens.projectsList

import com.example.noteautomatic.foundation.base.BaseViewModel
import com.example.noteautomatic.foundation.base.LiveResult
import com.example.noteautomatic.foundation.base.MutableLiveResult
import com.example.noteautomatic.foundation.base.PendingResult
import com.example.noteautomatic.foundation.base.SuccessResult
import com.example.noteautomatic.foundation.database.entities.Project
import com.example.noteautomatic.foundation.model.project.ProjectsListener
import com.example.noteautomatic.foundation.model.project.ProjectsRepository

class ProjectsListViewModel(
    private val projectsRepository: ProjectsRepository
) : BaseViewModel() {

    private val _projects = MutableLiveResult<List<Project>>(PendingResult())
    val projects: LiveResult<List<Project>> = _projects

    private val listener: ProjectsListener = {
        _projects.postValue(SuccessResult(it))
    }

    init {
        loadProjects()
    }

    override fun onCleared() {
        super.onCleared()
        projectsRepository.removeListener(listener)
    }

    fun tryAgain() {
        loadProjects()
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

    fun selectMoreProject(project: Project): Boolean {
        return projectsRepository.selectMoreProjects(project)
    }

    private fun loadProjects() {
        projectsRepository.addListener(listener)
    }

}