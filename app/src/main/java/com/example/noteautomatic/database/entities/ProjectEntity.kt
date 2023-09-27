package com.example.noteautomatic.database.entities

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
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val speed: Int,
    val play: Boolean = false,
){
    fun toProject(): Project = Project(
        id = id,
        name = name,
        play = play,
    )

    fun toFullProject(): FullProject = FullProject(
        id = id,
        name = name,
        speed = speed,
    )

    companion object {
        fun toProjectEntity(project: FullProject): ProjectEntity = ProjectEntity(
            id = project.id,
            name = project.name,
            speed = project.speed,
            play = !project.listImage.isNullOrEmpty()
        )
    }
}
