package com.example.noteautomatic.model.project

import com.example.noteautomatic.database.classes.FullProject
import com.example.noteautomatic.database.classes.Project
import com.example.noteautomatic.database.dao.ImageDao
import com.example.noteautomatic.database.dao.ProjectDao
import com.example.noteautomatic.database.entities.ProjectEntity
import com.example.noteautomatic.foundation.ProjectNotFoundException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RoomProjectsRepository(
    private val projectDao: ProjectDao,
    private val imageDao: ImageDao,
    private val ioDispatcher: CoroutineDispatcher
) : ProjectsRepository, CoroutineScope {

    private var projects = mutableListOf<Project>()

    private val listeners = mutableSetOf<ProjectsListener>()

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = ioDispatcher + job

    init {
        launch {
            withContext(Dispatchers.Main) {
                projectDao.getAllProjects().collect { projectsFromDatabase ->
                    projects = if (projectsFromDatabase.isEmpty()) mutableListOf()
                    else projectsFromDatabase.toMutableList()
                    notifyChanges()
                }
            }
        }
    }

    override fun updateProject(project: ProjectEntity) {
        launch {
            projectDao.insertOrUpdateProject(project)
            if (project.id == 0L) {
                projectDao.getAllProjects().collect { projectsFromDatabase ->
                    projects = if (projectsFromDatabase.isEmpty()) mutableListOf()
                    else projectsFromDatabase.toMutableList()
                    notifyChanges()
                }
                val index = projects.indexOfFirst { it.id == project.id }
                if (index != -1)
                    projects[index] = Project(project.id, project.name)
                notifyChanges()
            }
        }
    }

    override fun getNames(name: String, id: Long): String {
        val index = projects.indexOfFirst { it.name == name }
        return if ((index == -1) or (index.toLong() == id))
            name else ""
    }

    override suspend fun getById(id: Long): FullProject? = withContext(Dispatchers.IO) {
        try {
            val project =
                projects.firstOrNull { it.id == id } ?: throw ProjectNotFoundException()
            val fullProject = projectDao.getFullProject(id)?.toFullProject()

            if (fullProject != null) {
                imageDao.getAllImages(id).collect {
                    fullProject.listImage = it
                }
            }
            return@withContext fullProject
        } catch (e: Exception) {
            null
        }
    }


    override fun deleteProject(id: Long) {
        launch {
            val indexToDelete = projects.indexOfFirst { it.id == id }
            if (indexToDelete != -1) {
                projects = ArrayList(projects)
                projects.removeAt(indexToDelete)
                notifyChanges()
            }
            projectDao.deleteProjects(listOf(id))
        }
    }

    override fun deleteProjects() {
        launch {
            projectDao.deleteProjects(projects.filter { it.selected == true }
                .map { project -> project.id })
            projects = projects.filter { it.selected == false }.toMutableList()
            selectAllProjects(null)
        }
    }

    override fun selectAllProjects(selected: Boolean?) {
        projects = ArrayList(projects)
        projects.forEachIndexed { i, pr ->
            projects[i] = pr.copy(selected = selected)
        }
        notifyChanges()
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
