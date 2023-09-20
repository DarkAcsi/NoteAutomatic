package com.example.noteautomatic.screens.projectsList.projects

data class Project(
    val id: Long,
//    var position: Long,
    val name: String,
    val listPages: MutableList<Int>?
)
