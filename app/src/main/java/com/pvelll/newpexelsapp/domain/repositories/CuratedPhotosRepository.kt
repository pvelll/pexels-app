package com.pvelll.newpexelsapp.domain.repositories

import com.pvelll.newpexelsapp.domain.models.CuratedPhotosResponse

interface CuratedPhotosRepository {
    suspend fun getCuratedPhotos() : CuratedPhotosResponse
}