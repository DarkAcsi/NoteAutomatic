package com.example.noteautomatic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.noteautomatic.database.dao.ImageDao
import com.example.noteautomatic.database.dao.ProjectDao
import com.example.noteautomatic.database.entities.ImageEntity
import com.example.noteautomatic.database.entities.ProjectEntity

@Database(
    version = 1,
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