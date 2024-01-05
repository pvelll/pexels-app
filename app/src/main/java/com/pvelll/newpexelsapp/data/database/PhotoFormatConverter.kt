package com.pvelll.newpexelsapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pvelll.newpexelsapp.data.model.Src

class PhotoFormatConverter {
    @TypeConverter
    fun fromPhotoFormat(src: Src): String {
        return Gson().toJson(src)
    }

    @TypeConverter
    fun toPhotoFormat(srcString: String): Src {
        return Gson().fromJson(srcString, Src::class.java)
    }
}