package com.pvelll.newpexelsapp.domain.repositories

import com.pvelll.newpexelsapp.data.model.Photo
import java.io.File

interface BookmarksStorageRepository : StorageRepository {
    suspend fun saveToBookmarks(photo : Photo, file : File)
}