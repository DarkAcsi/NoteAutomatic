package com.example.noteautomatic.foundation.model.image

import com.example.noteautomatic.foundation.database.entities.Image

typealias ImagesListener = (images: List<Image>) -> Unit

interface ImagesRepository {

    // with database
    suspend fun loadImages(projectId: Long): List<Image>

    suspend fun saveImages(projectId: Long)

    fun deleteImage(id: Long)

    // without database
    fun moveImage(fromPosition: Int, toPosition: Int)

    fun localUpdate(listImage: List<Image>)

    // other
    fun addListener(listener: ImagesListener)

    fun removeListener(listener: ImagesListener)
}