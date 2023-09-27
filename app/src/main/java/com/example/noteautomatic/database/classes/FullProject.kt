package com.example.noteautomatic.database.classes

data class FullProject(
    val id: Long,
    val name: String,
    var speed: Int = 0,
    var listImage: List<Image>? = null
)