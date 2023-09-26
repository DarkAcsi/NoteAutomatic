package com.example.noteautomatic.database

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.lang.reflect.Type

class Converter {

    @TypeConverter
    fun toUri(value: String?): Uri? {
        val listType: Type = object : TypeToken<Uri?>() {}.type
        return Gson().fromJson(value,listType)
    }

    @TypeConverter
    fun fromUri(uri: Uri?) : String?{
        return Gson().toJson(uri)
    }

}