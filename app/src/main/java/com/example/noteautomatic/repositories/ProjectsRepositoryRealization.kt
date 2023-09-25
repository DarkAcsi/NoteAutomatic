package com.example.noteautomatic.repositories

import android.util.Log
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

    override fun selectAllProjects(selected: Boolean?) {
        projects = ArrayList(projects)
        projects.forEachIndexed { i, pr ->
            projects[i] = pr.copy(selected = selected)
        }
        notifyChanges()
        Log.d("aaa", "selectAllProjects")
    }

    override fun selectProjects(project: Project, selected: Boolean) {
        val index = projects.indexOfFirst { it.id == project.id }
        selectAllProjects(if (selected) false else null)
        if (index == -1) return
        projects[index] = projects[index].copy(selected = if (selected) true else null)
        notifyChanges()
    }

    override fun selectMoreProjects(project: Project): Boolean {
        val index = projects.indexOfFirst { it.id == project.id }
        val count = projects.count { it.selected == true && it.id != project.id }
        if (count == 0) {
            selectProjects(project, false)
            return false
        }
        projects = ArrayList(projects)
        projects[index] = projects[index].copy(selected = project.selected != true)
        notifyChanges()
        return true
    }

    override fun getById(id: Long): FullProject {
        if (id == 0.toLong()) return FullProject(Project(0, ""))
        val project = projects.firstOrNull { it.id == id } ?: throw ProjectNotFoundException()
        return FullProject(
            project = project
        )
    }

    override fun deleteProject(project: Project) {
        val indexToDelete = projects.indexOfFirst { it.id == project.id }
        if (indexToDelete == -1) return

        projects = ArrayList(projects)
        projects.removeAt(indexToDelete)
        notifyChanges()
    }

    override fun deleteProjects() {
        projects = projects.filter { it.selected == false }.toMutableList()
        selectAllProjects(null)
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
