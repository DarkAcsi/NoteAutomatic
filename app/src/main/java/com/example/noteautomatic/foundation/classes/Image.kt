package com.example.noteautomatic.foundation.classes

import android.net.Uri
import androidx.room.ColumnInfo

data class Image(
    val id: Long,
    @ColumnInfo(name = "project_id") val projectId: Long,
    val position: Int,
    @ColumnInfo(name = "res_image") val resImage: Uri?,
    val file: Boolean = false
)
