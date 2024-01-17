package com.pvelll.newpexelsapp.data.repository

import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.domain.repositories.BookmarksStorageRepository
import java.io.File

class BookmarksStorageRepositoryImpl : BookmarksStorageRepository {
    override suspend fun downloadPhoto(url: String, file: File) {
        TODO("Not yet implemented")
    }

    override suspend fun saveToBookmarks(photo: Photo, file: File) {
        TODO("Not yet implemented")
    }

    override suspend fun removePhoto(photo: Photo) {
        TODO("Not yet implemented")
    }
}