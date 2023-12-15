package com.pvelll.newpexelsapp.domain.repositories

import com.pvelll.newpexelsapp.domain.models.Photo

interface PhotoByIdRepository {
    suspend fun getPhotoById(id : Int) : Photo
}