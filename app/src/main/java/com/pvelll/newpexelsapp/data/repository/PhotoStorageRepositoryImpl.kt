package com.pvelll.newpexelsapp.data.repository

import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.domain.repositories.PhotoStorageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class PhotoStorageRepositoryImpl : PhotoStorageRepository {
    private val dbRepo = DatabaseRepositoryImpl()
    override suspend fun downloadPhoto(url: String, file: File) {
        CoroutineScope(Dispatchers.IO).launch {
            val bytes = URL(url).readBytes()
            val outputStream = FileOutputStream(file)
            outputStream.use { it.write(bytes) }
        }
    }

    override suspend fun saveToBookmarks(photo: Photo, file: File) {
        downloadPhoto(photo.src.large2x, file)
        dbRepo.insertPhoto(photo)
    }

    override suspend fun removePhoto(photo: Photo, file: File) {
        dbRepo.removePhoto(photo)
        if (file.exists()) {
            file.delete()
        }
    }
}