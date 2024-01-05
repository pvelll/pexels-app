package com.pvelll.newpexelsapp.data.repository


import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.domain.models.PhotoGalleryResponse
import com.pvelll.newpexelsapp.domain.repositories.PhotoGalleryRepository


class PhotoGalleryRepositoryImpl(private val api: PexelApi): PhotoGalleryRepository {
    override suspend fun getPhotoGallery(): PhotoGalleryResponse {
        val response = api.getPhotoGallery()
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