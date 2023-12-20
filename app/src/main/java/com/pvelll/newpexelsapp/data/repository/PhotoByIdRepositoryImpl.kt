package com.pvelll.newpexelsapp.data.repository

import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.domain.repositories.PhotoByIdRepository

class PhotoByIdRepositoryImpl(private val api : PexelApi): PhotoByIdRepository {
    override suspend fun getPhotoById(id : Int): Photo {
        val response = api.getPhotoById(id)
        when(response.isSuccessful){
            true -> {
                return response.body() ?: throw Exception("response photo bu id is null")
            }
            else -> {
                throw Exception("Response wasn't successful: ${response.code()} ")
            }
        }
    }
}