package com.pvelll.newpexelsapp.data.repository

import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.di.appModule
import com.pvelll.newpexelsapp.domain.models.PhotosResponse
import com.pvelll.newpexelsapp.domain.repositories.PhotosRepository

class PhotosRepositoryImpl(private val api : PexelApi) : PhotosRepository {
    override suspend fun getPhotos(query: String) : PhotosResponse{
        val response = api.getPhotos(query)
        when(response.isSuccessful){
            true -> {
                return response.body() ?: throw Exception("photos response body is null")
            }
            else -> {
                throw Exception("Response wasn't successful: ${response.code()} ")
            }
        }
    }

}