package com.pvelll.newpexelsapp.ui.viewmodels

import android.app.Application
import android.content.Context
import android.media.tv.interactive.AppLinkInfo
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pvelll.newpexelsapp.R
import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.data.database.PhotoDatabase
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.data.repository.DatabaseRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotoByIdRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotoStorageRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

class DetailsViewModel(
    private val databaseRepo: DatabaseRepositoryImpl,
    private val photoStorageRepo: PhotoStorageRepositoryImpl,
    private val connectivityObserver: NetworkConnectivityObserver,
) : ViewModel() {
    private val _photo = MutableLiveData<Photo>()
    val photo: LiveData<Photo>
        get() = _photo

    private val _isBookmarked = MutableLiveData<Boolean>()
    val isBookmarked: LiveData<Boolean>
        get() = _isBookmarked

    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean>
        get() = _isNetworkAvailable

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun init(photo: Photo) {
        _photo.value = photo
        _isNetworkAvailable.value = connectivityObserver.isConnected()
        checkPhotoSaved()
    }

    private fun checkPhotoSaved() {
        viewModelScope.launch {
            val existingPhoto = photo.value?.let { databaseRepo.getPhoto(it.id) }
            _isBookmarked.value = existingPhoto != null
        }
    }

    fun saveToBookmarks(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            if (_isBookmarked.value == true) {
                removeFromBookmarks(context)
            } else {
                if (_isNetworkAvailable.value == connectivityObserver.isConnected()) {
                    viewModelScope.launch {
                        val file = File(
                            context.getExternalFilesDir(null),
                            "${photo.value?.id}.jpeg"
                        )
                        try {
                            if (file.exists()) {
                                _errorMessage.value = context.resources.getString(
                                    R.string.photo_exists
                                )
                            } else {
                                photo.value?.let {
                                    photoStorageRepo.saveToBookmarks(it, file)
                                    _isBookmarked.value = true
                                    _errorMessage.value =
                                        context.resources.getString(R.string.photo_successfully_saved_bookmarks)
                                }
                            }
                        } catch (e: IOException) {
                            _errorMessage.value =
                                context.resources.getString(R.string.error_saving_photo)
                        }
                    }
                } else {
                    _errorMessage.value = context.resources.getString(R.string.no_internet)
                }

            }
        }
    }

    fun downloadPhoto(context: Context) {
        viewModelScope.launch {
            val directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(directory, "${photo.value?.id}.jpeg")
            try {
                photo.value?.let {
                    photoStorageRepo.downloadPhoto(it.src.original, file)
                    _errorMessage.value = context.resources.getString(R.string.photo_downloaded)

                }
            } catch (e: IOException) {
                _errorMessage.value = context.resources.getString(R.string.error_saving_photo)
            }
        }
    }

    private fun removeFromBookmarks(context: Context) {
        viewModelScope.launch {
            photo.value?.let { photo ->
                val file = File(
                    context.getExternalFilesDir(null),
                    "${photo.id}.jpeg"
                )
                photoStorageRepo.removePhoto(photo, file)
                _isBookmarked.value = false
                _errorMessage.value =
                    context.resources.getString(R.string.photo_removed_from_bookmarks)
            }
        }
    }

}