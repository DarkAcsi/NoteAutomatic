package com.example.noteautomatic.database.classes

data class FullProject(
    val id: Long,
    val name: String,
    val speed: Int = 0,
    val file: String? = null,
    var listImage: List<Image>? = null
)