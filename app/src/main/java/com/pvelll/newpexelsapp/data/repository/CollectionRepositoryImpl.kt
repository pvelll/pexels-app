package com.pvelll.newpexelsapp.data.repository

import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.domain.models.FeaturedCollectionResponse
import com.pvelll.newpexelsapp.domain.repositories.CollectionRepository

class CollectionRepositoryImpl(private val api: PexelApi): CollectionRepository {
    override suspend fun getCollection(): FeaturedCollectionResponse {
        val response = api.getFeaturedCollection()
        when(response.isSuccessful){
            true -> {
                return response.body() ?: throw Exception("collection response is null")
            }
            else ->  {
                throw Exception("Response wasn't successful: ${response.code()} ")
            }
        }
    }
}