package com.pvelll.newpexelsapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pvelll.newpexelsapp.data.model.PhotoFormat

class PhotoFormatConverter {
    @TypeConverter
    fun fromPhotoFormat(photoFormat: PhotoFormat): String {
        return Gson().toJson(photoFormat)
    }

    @TypeConverter
    fun toPhotoFormat(srcString: String): PhotoFormat {
        return Gson().fromJson(srcString, PhotoFormat::class.java)
    }
}