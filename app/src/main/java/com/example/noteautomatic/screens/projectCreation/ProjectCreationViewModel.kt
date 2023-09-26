package com.example.noteautomatic.screens.projectCreation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteautomatic.database.classes.FullProject
import com.example.noteautomatic.model.ProjectNotFoundException
import com.example.noteautomatic.model.project.ProjectsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectCreationViewModel(
    private val projectsRepository: ProjectsRepository
) : ViewModel() {

    private val _fullProject = MutableLiveData<FullProject>()
    val fullProject: LiveData<FullProject>? = _fullProject

    fun loadProject(projectId: Long) {
        if (projectId != 0L) {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    _fullProject.value = projectsRepository.getById(projectId)
                }
            }
        }
        else {
            _fullProject.value = FullProject(0, "New Project")
        }
    }


}