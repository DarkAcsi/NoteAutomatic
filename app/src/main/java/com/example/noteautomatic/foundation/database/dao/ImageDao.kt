package com.example.noteautomatic.foundation.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.noteautomatic.foundation.database.entities.Image

@Dao
interface ImageDao {

    @Query("SELECT * FROM images WHERE project_id = :projectId ORDER BY position")
    fun getAllImages(projectId: Long): List<Image>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateImages(images: List<Image>)

    @Query("DELETE FROM images WHERE id = :id")
    fun deleteImage(id: Long)

    @Query("DELETE FROM images WHERE project_id = :projectId")
    fun deleteImages(projectId: Long)

}