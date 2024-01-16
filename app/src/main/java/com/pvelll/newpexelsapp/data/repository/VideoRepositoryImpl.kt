package com.pvelll.newpexelsapp.data.repository

import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.domain.models.VideoResponse
import com.pvelll.newpexelsapp.domain.repositories.VideoRepository

class VideoRepositoryImpl(private val api : PexelApi) : VideoRepository {
    override suspend fun getVideos(query: String): VideoResponse {
        val response = api.getVideos(query)
        when(response.isSuccessful){
            true -> {
                return response.body() ?: throw Exception("Video response is not successful")
            }
            else -> {
                throw Exception("Response wasn't successful: ${response.code()} ")
            }
        }
    }
}