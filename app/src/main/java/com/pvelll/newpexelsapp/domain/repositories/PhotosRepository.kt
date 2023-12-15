package com.pvelll.newpexelsapp.domain.repositories

import com.pvelll.newpexelsapp.domain.models.PhotosResponse

interface PhotosRepository {
    suspend fun getPhotos(query: String): PhotosResponse
}
