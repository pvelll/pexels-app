package com.pvelll.newpexelsapp.domain.repositories

import com.pvelll.newpexelsapp.domain.models.VideoResponse

interface VideoRepository {
    suspend fun getVideos(query: String) : VideoResponse
}