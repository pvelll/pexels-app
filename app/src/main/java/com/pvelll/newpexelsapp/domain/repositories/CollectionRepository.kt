package com.pvelll.newpexelsapp.domain.repositories

import com.pvelll.newpexelsapp.domain.models.FeaturedCollectionResponse

interface CollectionRepository {
    suspend fun getCollection(): FeaturedCollectionResponse
}