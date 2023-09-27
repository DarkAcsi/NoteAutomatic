package com.example.noteautomatic.model.project

import com.example.noteautomatic.database.classes.FullProject
import com.example.noteautomatic.database.classes.Project
import com.example.noteautomatic.database.entities.ProjectEntity

typealias ProjectsListener = (projects: List<Project>) -> Unit

interface ProjectsRepository {

    // with database
    suspend fun getById(id: Long): FullProject?

    fun deleteProject(id: Long)

    fun deleteProjects()

    fun updateProject(project: ProjectEntity)

    fun getNames(name: String, id: Long): String

    // without database
    fun selectAllProjects(selected: Boolean?)

    fun selectProjects(project: Project, selected: Boolean)

    fun selectMoreProjects(project: Project): Boolean

    // other
    fun addListener(listener: ProjectsListener)

    fun removeListener(listener: ProjectsListener)
}
