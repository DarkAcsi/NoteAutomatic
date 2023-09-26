package com.example.noteautomatic

import android.content.Context
import androidx.room.Room
import com.example.noteautomatic.database.AppDatabase
import com.example.noteautomatic.model.image.ImagesRepository
import com.example.noteautomatic.model.image.RoomImagesRepository
import com.example.noteautomatic.model.project.ProjectsRepository
import com.example.noteautomatic.model.project.RoomProjectsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

object Repositories {

    private lateinit var applicationContext: Context

    val database: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    val projectsRepository: ProjectsRepository by lazy {
        RoomProjectsRepository(database.getProjectDao(), database.getImageDao(), ioDispatcher)
    }

    val imagesRepository: ImagesRepository by lazy {
        RoomImagesRepository(database.getProjectDao(), database.getImageDao(), ioDispatcher)
    }

    fun init(context: Context) {
        applicationContext = context
    }

}