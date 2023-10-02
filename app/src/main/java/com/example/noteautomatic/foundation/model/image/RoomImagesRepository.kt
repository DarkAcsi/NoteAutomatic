package com.example.noteautomatic.foundation.model.image

import android.util.Log
import com.example.noteautomatic.foundation.database.dao.ImageDao
import com.example.noteautomatic.foundation.database.entities.Image
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


    override suspend fun loadImages(projectId: Long): List<Image> {
        return withContext(Dispatchers.IO) {
            val imagesProject = imageDao.getAllImages(projectId)
            images = ArrayList(imagesProject)
            notifyChanges()
            return@withContext imagesProject
        }
    }

    override suspend fun saveImages(projectId: Long) {
        withContext(Dispatchers.IO) {
            images = images.mapIndexed { index, it ->
                it.copy(projectId = projectId, position = index)
            }.toMutableList()
            imageDao.insertOrUpdateImages(images)
        }
    }

    override fun deleteImage(id: Long) {
        launch {
            val indexToDelete = images.indexOfFirst { it.id == id }
            if (indexToDelete != -1) {
                images = ArrayList(images)
                images.removeAt(indexToDelete)
                notifyChanges()
            }
            imageDao.deleteImage(id)
        }
    }

    override fun moveImage(fromPosition: Int, toPosition: Int) {
        val image = images[fromPosition]
        images = ArrayList(images)
        images.removeAt(fromPosition)
        images.add(toPosition, image)
        notifyChanges()
    }

    override fun localUpdate(listImage: List<Image>) {
        images = ArrayList(images + listImage).mapIndexed { index, it ->
            it.copy(position = index)
        }.toMutableList()
        notifyChanges()
    }

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
