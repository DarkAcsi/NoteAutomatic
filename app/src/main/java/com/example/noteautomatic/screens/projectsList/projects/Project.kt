package com.example.noteautomatic.screens.projectsList.projects

data class Project(
    val id: Long,
//    var position: Long,
    val name: String
)

data class FullProject(
    val project: Project,
    val listPages: List<Long>? = null,
    val timePage: Long? = null,
    val timeStart: Long? = null,
    val timePause: Long? = null
)
