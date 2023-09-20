package com.example.noteautomatic

import android.app.Application
import com.example.noteautomatic.model.ProjectsService

class App : Application() {

    val projectsService = ProjectsService()
}