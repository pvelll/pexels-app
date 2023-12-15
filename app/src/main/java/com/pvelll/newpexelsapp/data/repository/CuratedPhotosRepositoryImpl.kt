package com.pvelll.newpexelsapp.data.repository

import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.domain.models.CuratedPhotosResponse
import com.pvelll.newpexelsapp.domain.repositories.CuratedPhotosRepository

class CuratedPhotosRepositoryImpl(private val api : PexelApi): CuratedPhotosRepository {
    override suspend fun getCuratedPhotos(): CuratedPhotosResponse {
        val response = api.getCuratedPictures()
        when(response.isSuccessful){
            true -> {
                return response.body() ?: throw Exception(" curated photos response is null")
            }
            else -> {
                throw Exception("Response wasn't successful: ${response.code()} ")
            }
        }
    }
}