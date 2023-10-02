package com.example.noteautomatic.database.entities

import android.net.Uri
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.noteautomatic.database.classes.FullProject
import com.example.noteautomatic.database.classes.Project

@Entity(
    tableName = "projects",
    indices = [Index(value = ["name"], unique = true)]
)
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val speed: Int,
    val file: Uri? = null
){
    fun toProject(): Project = Project(
        id = id,
        name = name,
        selected = null
    )

    fun toFullProject(): FullProject = FullProject(
        id = id,
        name = name,
        speed = speed,
        file = file,
    )

    companion object {
        fun toProjectEntity(project: FullProject): ProjectEntity = ProjectEntity(
            id = project.id,
            name = project.name,
            speed = project.speed,
            file = project.file
        )
    }
}
