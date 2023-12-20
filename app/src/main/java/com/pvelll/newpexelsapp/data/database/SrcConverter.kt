package com.pvelll.newpexelsapp.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.pvelll.newpexelsapp.data.model.Src

class SrcConverter {
    @TypeConverter
    fun fromSrc(src: Src): String {
        return Gson().toJson(src)
    }

    @TypeConverter
    fun toSrc(srcString: String): Src {
        return Gson().fromJson(srcString, Src::class.java)
    }
}