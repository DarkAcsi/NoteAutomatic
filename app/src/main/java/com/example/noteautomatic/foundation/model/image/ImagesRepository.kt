package com.example.noteautomatic.foundation.model.image

import com.example.noteautomatic.foundation.classes.Image

typealias ImagesListener = (images: List<Image>) -> Unit

interface ImagesRepository {


    // other
    fun addListener(listener: ImagesListener)

    fun removeListener(listener: ImagesListener)
}