package com.example.noteautomatic.screens.projectCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteautomatic.database.classes.FullProject
import com.example.noteautomatic.database.entities.ProjectEntity
import com.example.noteautomatic.model.project.ProjectsRepository
import kotlinx.coroutines.launch

class ProjectCreationViewModel(
    private val projectsRepository: ProjectsRepository
) : ViewModel() {

    private val _fullProject = MutableLiveData<FullProject>()
    val fullProject: LiveData<FullProject> = _fullProject
    private var projectId: Long = 0L

    fun loadProject(id: Long) {
        projectId = id
        viewModelScope.launch {
            if (projectId != 0L) {
                _fullProject.postValue(projectsRepository.getById(projectId))
            } else {
                _fullProject.postValue(FullProject(0, ""))
            }
        }
    }

    fun setNameProject(nameEditText: String, nameTextView: String, newProject: Boolean): String {
        return if (nameEditText.isEmpty()) {
            if (newProject) "" else nameTextView
        } else {
            if (projectsRepository.getNames(nameEditText, _fullProject.value?.id ?: -1)
                    .isBlank()
            ) nameTextView else nameEditText
        }
    }

    fun save(name: String, speed: Int, newProject: Boolean): FullProject? {
        val fullProjectBase: FullProject = fullProject.value ?: FullProject(0, "")
        if (newProject){
            val project = ProjectEntity(
                id = 0,
                name = name,
                speed = speed,
                play = !fullProjectBase.listImage.isNullOrEmpty()
            )
            viewModelScope.launch {
                projectsRepository.updateProject(project)
            }
            return null
        }
        val project = FullProject(
            id = fullProjectBase.id,
            name = name,
            speed = speed,
            listImage = fullProjectBase.listImage
        )
        _fullProject.postValue(project)
        viewModelScope.launch {
            projectsRepository.updateProject(ProjectEntity.toProjectEntity(project))
        }
        return project
    }
}