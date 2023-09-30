package com.example.noteautomatic.foundation.classes

data class Project(
    val id: Long,
    val name: String,
    val play: Boolean = false,
    val selected: Boolean? = null,
)

