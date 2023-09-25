package com.example.noteautomatic.screens.projectsList

data class Project(
    val id: Long,
//    var position: Long,
    val name: String,
    val selected: Boolean? = null
)

data class FullProject(
    val project: Project,
    val listPages: List<Long>? = null,
    val timePage: Long? = null,
    val timeStart: Long? = null,
    val timePause: Long? = null
)

