package com.example.noteautomatic.screens

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.noteautomatic.App
import com.example.noteautomatic.screens.projectsList.ProjectsListViewModel

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            ProjectsListViewModel::class.java -> {
                ProjectsListViewModel(app.projectsService)
            }

            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)