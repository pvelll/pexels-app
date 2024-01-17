package com.pvelll.newpexelsapp.data.repository

import androidx.lifecycle.LiveData
import com.pvelll.newpexelsapp.data.dao.PhotoDao
import com.pvelll.newpexelsapp.data.database.PhotoDatabase
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.domain.repositories.DatabaseRepository
import org.koin.java.KoinJavaComponent.inject

class DatabaseRepositoryImpl(): DatabaseRepository {
    private val db by inject<PhotoDatabase>(PhotoDatabase::class.java)
    override suspend fun getPhotos(): LiveData<List<Photo>> {
        return db.photoDao().getaAll()
    }

    override suspend fun insertPhoto(photo: Photo) {
        db.photoDao().insert(photo)
    }

    override suspend fun removePhoto(photo: Photo) {
        db.photoDao().delete(photo)
    }

    override suspend fun getPhoto(id: Int): Photo? {
        return  db.photoDao().getById(id)
    }
}