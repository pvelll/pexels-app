package com.pvelll.newpexelsapp.domain.repositories

import com.pvelll.newpexelsapp.domain.models.PhotoGalleryResponse

interface PhotoGalleryRepository {
    suspend fun getPhotoGallery(): PhotoGalleryResponse
}