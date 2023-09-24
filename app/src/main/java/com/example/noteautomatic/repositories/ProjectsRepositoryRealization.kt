package com.example.noteautomatic.repositories

import com.example.noteautomatic.screens.projectsList.FullProject
import com.example.noteautomatic.screens.projectsList.Project

class ProjectsRepositoryRealization : ProjectsRepository {

    private var projects = mutableListOf<Project>()

    private val listeners = mutableSetOf<ProjectsListener>()

    init {
        projects = (1..100).map {
            Project(
                id = it.toLong(),
//            position = it.toLong(),
                name = "project $it"
            )
        }.toMutableList()
    }

    override fun getProjects(): List<Project> {
        return projects
    }

    override fun getById(id: Long): FullProject {
        val project = projects.firstOrNull { it.id == id } ?: throw ProjectNotFoundException()
        return FullProject(
            project = project
        )
    }

    override fun deleteProject(project: Project) {
        val indexToDelete = projects.indexOfFirst { it.id == project.id }
        if (indexToDelete != -1) {
            projects = ArrayList(projects)
            projects.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    override fun addListener(listener: ProjectsListener) {
        listeners.add(listener)
        listener.invoke(projects)
    }

    override fun removeListener(listener: ProjectsListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(projects) }
    }
}
