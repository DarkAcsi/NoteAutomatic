package com.example.noteautomatic.foundation.model.image

import com.example.noteautomatic.foundation.database.dao.ImageDao
import com.example.noteautomatic.foundation.database.dao.ProjectDao
import kotlinx.coroutines.CoroutineDispatcher

class RoomImagesRepository(
    private val projectDao: ProjectDao,
    private val imageDao: ImageDao,
    private val ioDispatcher: CoroutineDispatcher
) : ImagesRepository {

}