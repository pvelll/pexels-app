package com.pvelll.newpexelsapp.data.database

import android.content.Context
import androidx.room.*
import com.pvelll.newpexelsapp.data.dao.PhotoDao
import com.pvelll.newpexelsapp.data.model.Photo

@Database(entities = [Photo::class], version = 1)
@TypeConverters(PhotoFormatConverter::class)
abstract class PhotoDatabase : RoomDatabase(){
    abstract fun photoDao(): PhotoDao
    companion object {
        @Volatile
        private var INSTANCE: PhotoDatabase? = null

        fun getDatabase(context: Context): PhotoDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhotoDatabase::class.java,
                    "photos"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}