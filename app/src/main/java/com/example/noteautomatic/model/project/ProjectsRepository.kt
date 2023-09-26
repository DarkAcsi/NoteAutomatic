package com.example.noteautomatic.model.project

import com.example.noteautomatic.database.classes.FullProject
import com.example.noteautomatic.database.classes.Project

typealias ProjectsListener = (projects: List<Project>) -> Unit

interface ProjectsRepository {

    // with database
    suspend fun getById(id: Long): FullProject?

    fun deleteProject(id: Long)

    fun deleteProjects()

    fun addProject(project: FullProject)

    fun updateProject(project: FullProject)

    suspend fun getSameNameProject(name: String): Boolean

    // without database
    fun selectAllProjects(selected: Boolean?)

    fun selectProjects(project: Project, selected: Boolean)

    fun selectMoreProjects(project: Project): Boolean

    // other
    fun addListener(listener: ProjectsListener)

    fun removeListener(listener: ProjectsListener)

}
