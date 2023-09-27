package com.example.noteautomatic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.noteautomatic.database.classes.Project
import com.example.noteautomatic.database.entities.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT id, name, play FROM projects")
    fun getAllProjects(): Flow<List<Project>>

//    @Query("SElECT name FROM projects")
//    fun getNames(): Flow<List<String>>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    fun getFullProject(projectId: Long): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id IN (:indexes)")
    fun deleteProjects(indexes: List<Long>)
}