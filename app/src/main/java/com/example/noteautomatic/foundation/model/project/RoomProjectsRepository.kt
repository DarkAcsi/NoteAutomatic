package com.example.noteautomatic.foundation.model.project

import com.example.noteautomatic.foundation.database.dao.ImageDao
import com.example.noteautomatic.foundation.database.dao.ProjectDao
import com.example.noteautomatic.foundation.database.entities.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RoomProjectsRepository(
    private val projectDao: ProjectDao,
    private val imageDao: ImageDao
) : ProjectsRepository, CoroutineScope {

    private var projects = mutableListOf<Project>()

    private val listeners = mutableSetOf<ProjectsListener>()

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

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

    override suspend fun updateProject(project: Project): Project {
        return withContext(Dispatchers.IO) {
            val projectId = projectDao.insertOrUpdateProject(project)
            if (project.id == 0L) {
                projects.add(Project(projectId, project.name))

            } else {
                val index = projects.indexOfFirst { it.id == project.id }
                if (index != -1)
                    projects[index] = Project(project.id, project.name)
            }
            return@withContext project.copy(id = projectId)
        }
    }

    override suspend fun getNames(name: String, id: Long): Boolean {
        return withContext(Dispatchers.IO) {
            val names = projectDao.getNames()
            val cnt: Int = names?.count { name == it } ?: 0
            val projectId = projects.indexOfFirst { it.id == id }
            if ((cnt == 0) or (projectId.toLong() == id))
                return@withContext true
            return@withContext false
        }
    }

    override suspend fun getById(id: Long): Project {
        return withContext(Dispatchers.IO) {
            val index = projects.indexOfFirst { it.id == id }
            if (index == -1)
                return@withContext Project(0, "Input name")
            return@withContext projectDao.getFullProject(id) ?: Project(0, "Input name")
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
            val indexes = projects.filter { it.selected == true }.map { project -> project.id }
            projectDao.deleteProjects(indexes)
            indexes.forEach{
                imageDao.deleteImages(it)
            }
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
        listener.invoke(projects)
        listeners.add(listener)
    }

    override fun removeListener(listener: ProjectsListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(projects) }
    }
}
