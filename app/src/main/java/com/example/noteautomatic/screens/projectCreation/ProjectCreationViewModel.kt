package com.example.noteautomatic.screens.projectCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteautomatic.database.classes.FullProject
import com.example.noteautomatic.model.project.ProjectsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectCreationViewModel(
    private val projectsRepository: ProjectsRepository
) : ViewModel() {

    private val _fullProject = MutableLiveData<FullProject>()
    val fullProject: LiveData<FullProject> = _fullProject

    var newProject = false

    fun loadProject(projectId: Long) {
        if (projectId != 0L) {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    _fullProject.value = projectsRepository.getById(projectId)
                }
            }
        } else {
            _fullProject.value = FullProject(0, "New Project")
            newProject = true
        }
    }

    fun setNameProject(name: String, previousName: String): LiveData<String> {
        val result = MutableLiveData<String>()
        if (name.isEmpty()) {
            result.value = if (newProject) "" else previousName
        } else {
            viewModelScope.launch {
                result.postValue(
                    if (projectsRepository.getNames(name).isEmpty()) previousName else name
                )
            }
        }
        return result
    }

    fun save(name: String, speed: Int) : FullProject{
        val project =  FullProject(
            id = fullProject.value?.id ?: 0,
            name = name,
            speed = speed,
            file = fullProject.value?.file,
            listImage = fullProject.value?.listImage
        )
        viewModelScope.launch{
            projectsRepository.updateProject(project)
        }
        return project
    }
}