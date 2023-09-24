package com.example.noteautomatic

import android.app.Application
import com.example.noteautomatic.repositories.ProjectsRepositoryRealization

class App : Application() {

    val projectsService = ProjectsRepositoryRealization()
//    val repositories: List<Any> = listOf<Any>(
//        ProjectsRepositoryRealization(),
//    )
}