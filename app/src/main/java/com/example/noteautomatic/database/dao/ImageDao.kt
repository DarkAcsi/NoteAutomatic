package com.example.noteautomatic.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.noteautomatic.database.classes.Image
import com.example.noteautomatic.database.entities.ImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ImageDao {

    @Query("SELECT * FROM images WHERE project_id = :projectId ORDER BY position")
    fun getAllImages(projectId: Long): Flow<List<Image>>

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