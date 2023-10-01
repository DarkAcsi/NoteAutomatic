package com.example.noteautomatic.foundation.model.image

import com.example.noteautomatic.foundation.classes.FullProject
import com.example.noteautomatic.foundation.classes.Image
import com.example.noteautomatic.foundation.classes.Project
import com.example.noteautomatic.foundation.database.dao.ImageDao
import com.example.noteautomatic.foundation.database.entities.ProjectEntity
import com.example.noteautomatic.foundation.model.project.ProjectsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RoomImagesRepository(
    private val imageDao: ImageDao
) : ImagesRepository, CoroutineScope {

    private var images = mutableListOf<Image>()

    private val listeners = mutableSetOf<ImagesListener>()

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    override fun addListener(listener: ImagesListener) {
        listener.invoke(images)
        listeners.add(listener)
    }

    override fun removeListener(listener: ImagesListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        listeners.forEach { it.invoke(images) }
    }
}
