package com.example.noteautomatic.foundation.model.project

import com.example.noteautomatic.foundation.classes.FullProject
import com.example.noteautomatic.foundation.classes.Project
import com.example.noteautomatic.foundation.database.entities.ProjectEntity

typealias ProjectsListener = (projects: List<Project>) -> Unit

interface ProjectsRepository {

    // with database
    suspend fun updateProject(project: ProjectEntity) : ProjectEntity

    suspend fun getById(id: Long): FullProject?

    suspend fun getNames(name: String, id: Long): Boolean

    fun deleteProject(id: Long)

    fun deleteProjects()

    // without database
    fun selectAllProjects(selected: Boolean?)

    fun selectProjects(project: Project, selected: Boolean)

    fun selectMoreProjects(project: Project): Boolean

    // other
    fun addListener(listener: ProjectsListener)

    fun removeListener(listener: ProjectsListener)
}
