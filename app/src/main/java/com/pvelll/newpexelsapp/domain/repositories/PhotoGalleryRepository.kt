package com.pvelll.newpexelsapp.domain.repositories

import com.pvelll.newpexelsapp.domain.models.FeaturedCollectionResponse

interface PhotoGalleryRepository {
    suspend fun getCollection(): FeaturedCollectionResponse
}