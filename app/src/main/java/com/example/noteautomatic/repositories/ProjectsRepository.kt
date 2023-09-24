package com.example.noteautomatic.repositories

import com.example.noteautomatic.screens.projectsList.FullProject
import com.example.noteautomatic.screens.projectsList.Project

typealias ProjectsListener = (projects: List<Project>) -> Unit

interface ProjectsRepository {
    fun getProjects(): List<Project>

    fun getById(id: Long): FullProject

    fun deleteProject(project: Project)

    fun addListener(listener: ProjectsListener)

    fun removeListener(listener: ProjectsListener)

}
