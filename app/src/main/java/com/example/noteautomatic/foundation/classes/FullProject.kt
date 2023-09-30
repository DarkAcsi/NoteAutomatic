package com.example.noteautomatic.foundation.classes

data class FullProject(
    val id: Long,
    val name: String,
    var speed: Int = 0,
    var listImage: List<Image> = emptyList()
)