package com.example.noteautomatic.foundation.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.noteautomatic.foundation.database.dao.ImageDao
import com.example.noteautomatic.foundation.database.dao.ProjectDao
import com.example.noteautomatic.foundation.database.entities.Image
import com.example.noteautomatic.foundation.database.entities.Project


@Database(
    version = 8,
    entities = [
        Project::class,
        Image::class
    ]
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getProjectDao(): ProjectDao

    abstract fun getImageDao(): ImageDao

}