package com.example.noteautomatic.foundation.database

import android.net.Uri
import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun toUri(value: String): Uri {
        return Uri.parse(value)
    }

    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

}