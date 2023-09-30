package com.example.noteautomatic.foundation.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.noteautomatic.foundation.database.dao.ImageDao
import com.example.noteautomatic.foundation.database.dao.ProjectDao
import com.example.noteautomatic.foundation.database.entities.ImageEntity
import com.example.noteautomatic.foundation.database.entities.ProjectEntity

@Database(
    version = 3,
    entities = [
        ProjectEntity::class,
        ImageEntity::class
    ]
)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getProjectDao(): ProjectDao

    abstract fun getImageDao(): ImageDao

}