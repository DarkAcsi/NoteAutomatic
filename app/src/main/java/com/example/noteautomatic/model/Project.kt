package com.example.noteautomatic.model

data class Project(
    val id: Long,
//    var position: Long,
    val name: String,
    val listPages: MutableList<Int>?
)
