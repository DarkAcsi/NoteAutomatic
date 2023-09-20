package com.example.noteautomatic

import android.app.Application
import com.example.noteautomatic.screens.projectsList.projects.ProjectsService

class App : Application() {

    val projectsService = ProjectsService()
}