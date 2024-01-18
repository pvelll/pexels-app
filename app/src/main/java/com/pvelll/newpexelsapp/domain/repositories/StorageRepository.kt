package com.pvelll.newpexelsapp.domain.repositories

import com.pvelll.newpexelsapp.data.model.Photo
import java.io.File

interface StorageRepository {
    suspend fun downloadPhoto(url: String, file: File)
    suspend fun removePhoto(photo: Photo, file: File)
}