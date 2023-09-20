package com.example.noteautomatic.screens.projectsList.projects

typealias ProjectsListener = (projects: List<Project>) -> Unit

class ProjectsService {

    private var projects = mutableListOf<Project>()

    private val listeners = mutableSetOf<ProjectsListener>()

    init {
        projects = (1..100).map {
            Project(
                id = it.toLong(),
//            position = it.toLong(),
                name = "project $it",
                listPages = null
            )
        }.toMutableList()
    }

    fun getProjects(): List<Project> {
        return projects
    }

    fun deleteProject(project: Project) {
        val indexToDelete = projects.indexOfFirst { it.id == project.id }
        if (indexToDelete != -1) {
            projects = ArrayList(projects)
            projects.removeAt(indexToDelete)
            notifyChanges()
        }
    }

    fun moveUpProject(project: Project) {
        val oldIndex = projects.indexOfFirst { it.id == project.id }
        if (oldIndex != -1) {
            projects = ArrayList(projects)
            projects.removeAt(oldIndex)
            projects.add(0, project)
            notifyChanges()
        }
    }

    fun addListener(listener: ProjectsListener) {
        listeners.add(listener)
        listener.invoke(projects)
    }

    fun removeListener(listener: ProjectsListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(projects) }
    }

}
