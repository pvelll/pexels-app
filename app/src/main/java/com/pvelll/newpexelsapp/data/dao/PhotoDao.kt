package com.pvelll.newpexelsapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pvelll.newpexelsapp.data.model.Photo

@Dao
interface PhotoDao {
    @Query("SELECT * FROM photos")
    suspend fun getaAll(): LiveData<List<Photo>>

    @Query("SELECT * FROM photos WHERE id = :id")
    suspend fun getById(id: Int): Photo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<Photo>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: Photo)

    @Delete
    suspend fun delete(photo: Photo)
}