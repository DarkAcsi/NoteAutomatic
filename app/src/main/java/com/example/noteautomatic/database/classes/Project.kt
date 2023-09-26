package com.example.noteautomatic.database.classes

data class Project(
    val id: Long,
    val name: String,
    val selected: Boolean? = null
)

