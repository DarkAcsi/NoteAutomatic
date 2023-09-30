package com.example.noteautomatic.screens.projectRun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.noteautomatic.foundation.classes.FullProject
import com.example.noteautomatic.foundation.base.BaseViewModel
import com.example.noteautomatic.foundation.base.LiveResult
import com.example.noteautomatic.foundation.base.MutableLiveResult
import com.example.noteautomatic.foundation.model.project.ProjectsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectRunViewModel(
    private val projectsRepository: ProjectsRepository
) : BaseViewModel() {

    private val _projectRun = MutableLiveResult<FullProject>()
    val projectRun: LiveResult<FullProject> = _projectRun

    fun loadProject(projectId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
//                _projectRun.value = projectsRepository.getById(projectId)
            }
        }
    }


}