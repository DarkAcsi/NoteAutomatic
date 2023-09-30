package com.example.noteautomatic.foundation.database.entities

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "project_id") val projectId: Long,
    val position: Int,
    @ColumnInfo(name = "res_image") val resImage: Uri?,
    val file: Boolean
)