package com.example.noteautomatic.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.noteautomatic.database.classes.Image
import com.example.noteautomatic.database.classes.FullProject
import com.example.noteautomatic.database.classes.Project
import com.example.noteautomatic.database.entities.ImageEntity
import com.example.noteautomatic.database.entities.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT id, name FROM projects")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    fun getFullProject(projectId: Long): ProjectEntity?

    @Update(entity = ProjectEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updateProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id IN (:indexes)")
    fun deleteProjects(indexes: List<Long>)

    @Query("SElECT name FROM projects")
    fun getNames(): Flow<List<String>>

}