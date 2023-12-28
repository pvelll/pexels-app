package com.pvelll.newpexelsapp.data.repository

import androidx.lifecycle.LiveData
import com.pvelll.newpexelsapp.data.dao.PhotoDao
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.domain.repositories.DatabaseRepository

class DatabaseRepositoryImpl(private val photoDao: PhotoDao): DatabaseRepository {
    override fun getPhotos(): LiveData<List<Photo>> {
        return photoDao.getaAll()
    }

    override suspend fun insertPhoto(photo: Photo) {
        photoDao.insert(photo)
    }
}