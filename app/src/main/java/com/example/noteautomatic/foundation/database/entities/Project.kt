package com.example.noteautomatic.foundation.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "projects",
    indices = [Index(value = ["name"], unique = true)]
)
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    var speed: Int = 0,
    val play: Boolean = false,
    val selected: Boolean? = null
)
