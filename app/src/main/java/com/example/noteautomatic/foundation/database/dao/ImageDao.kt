package com.example.noteautomatic.foundation.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.noteautomatic.foundation.classes.Image
import com.example.noteautomatic.foundation.database.entities.ImageEntity

@Dao
interface ImageDao {

    @Query("SELECT * FROM images WHERE project_id = :projectId ORDER BY position")
    fun getAllImages(projectId: Long): List<Image>

    @Update(entity = ImageEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updateImagesPosition(images: List<Image>)

    @Insert(entity = ImageEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(image: Image)

    @Insert(entity = ImageEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertImages(images: List<Image>)

    @Query("DELETE FROM images WHERE id = :id")
    fun deleteImage(id: Long)

    @Query("DELETE FROM images WHERE project_id = :projectId")
    fun deleteImages(projectId: Long)

}