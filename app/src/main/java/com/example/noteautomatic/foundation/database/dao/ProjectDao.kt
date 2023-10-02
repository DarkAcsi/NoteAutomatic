package com.example.noteautomatic.foundation.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.noteautomatic.foundation.database.entities.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SElECT name FROM projects")
    fun getNames(): List<String>?

    @Query("SELECT * FROM projects WHERE id = :projectId")
    fun getFullProject(projectId: Long): Project?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateProject(project: Project): Long

    @Query("DELETE FROM projects WHERE id IN (:indexes)")
    fun deleteProjects(indexes: List<Long>)
}