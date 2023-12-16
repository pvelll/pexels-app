package com.pvelll.newpexelsapp.domain.repositories

import com.pvelll.newpexelsapp.data.model.Photo

interface PhotoByIdRepository {
    suspend fun getPhotoById(id : Int) : Photo
}