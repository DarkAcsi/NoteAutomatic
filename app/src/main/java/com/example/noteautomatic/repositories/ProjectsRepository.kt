package com.example.noteautomatic.repositories

import com.example.noteautomatic.screens.projectsList.FullProject
import com.example.noteautomatic.screens.projectsList.Project

typealias ProjectsListener = (projects: List<Project>) -> Unit

interface ProjectsRepository {
    fun getProjects(): List<Project>

    fun getById(id: Long): FullProject

    fun deleteProject(project: Project)

    fun deleteProjects()

    fun selectAllProjects(selected: Boolean?)

    fun selectProjects(project: Project, selected: Boolean)

    fun selectMoreProjects(project: Project): Boolean

    fun isAllSelected(): Boolean

    fun addListener(listener: ProjectsListener)

    fun removeListener(listener: ProjectsListener)

}
