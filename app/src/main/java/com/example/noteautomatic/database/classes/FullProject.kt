package com.example.noteautomatic.database.classes

import android.net.Uri

data class FullProject(
    val id: Long,
    val name: String,
    var speed: Int = 0,
    var file: Uri? = null,
    var listImage: List<Image>? = null
)