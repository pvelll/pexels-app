package com.pvelll.newpexelsapp.domain.repositories

import androidx.lifecycle.LiveData
import com.pvelll.newpexelsapp.data.dao.PhotoDao
import com.pvelll.newpexelsapp.data.model.Photo

interface DatabaseRepository{
    fun getPhotos(): LiveData<List<Photo>>
    suspend fun insertPhoto(photo: Photo)
}