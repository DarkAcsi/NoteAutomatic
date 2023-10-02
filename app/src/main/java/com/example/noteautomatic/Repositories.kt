package com.example.noteautomatic

import android.content.Context
import androidx.room.Room
import com.example.noteautomatic.foundation.database.AppDatabase
import com.example.noteautomatic.foundation.model.image.ImagesRepository
import com.example.noteautomatic.foundation.model.image.RoomImagesRepository
import com.example.noteautomatic.foundation.model.project.ProjectsRepository
import com.example.noteautomatic.foundation.model.project.RoomProjectsRepository

object Repositories {

    private lateinit var applicationContext: Context

    val database: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    val projectsRepository: ProjectsRepository by lazy {
        RoomProjectsRepository(database.getProjectDao(), database.getImageDao())
    }

    val imagesRepository: ImagesRepository by lazy {
        RoomImagesRepository(database.getImageDao())
    }

    fun init(context: Context) {
        applicationContext = context
    }

}