package com.pvelll.newpexelsapp.domain.repositories

import androidx.lifecycle.LiveData
import com.pvelll.newpexelsapp.data.dao.PhotoDao
import com.pvelll.newpexelsapp.data.model.Photo

interface DatabaseRepository{
    suspend fun getPhotos(): LiveData<List<Photo>>
    suspend fun insertPhoto(photo: Photo)

    suspend fun removePhoto(photo : Photo)

    suspend fun getPhoto(id: Int): Photo?
}