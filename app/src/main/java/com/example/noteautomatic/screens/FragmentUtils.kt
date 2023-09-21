package com.example.noteautomatic.screens

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.noteautomatic.App
import com.example.noteautomatic.Navigator
import com.example.noteautomatic.screens.projectRun.ProjectRunViewModel
import com.example.noteautomatic.screens.projectsList.ProjectsListViewModel

class ViewModelFactory(
    private val app: App
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when (modelClass) {
            ProjectsListViewModel::class.java -> {
                ProjectsListViewModel(app.projectsService)
            }

            ProjectRunViewModel::class.java -> {
                ProjectRunViewModel(app.projectsService)
            }

            else -> {
                throw IllegalStateException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator