package com.pvelll.newpexelsapp.ui.viewmodels

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.data.database.PhotoDatabase
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.data.repository.PhotoByIdRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val db by inject<PhotoDatabase>(PhotoDatabase::class.java)
    private val connectivityObserver by inject<NetworkConnectivityObserver>(NetworkConnectivityObserver::class.java)

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
            val existingPhoto = photo.value?.let { getPhotoFromDB(it.id) }
            _isBookmarked.value = existingPhoto != null
        }
    }

    fun saveToBookmarks() {
        viewModelScope.launch {
            val file = File(getApplication<Application>().getExternalFilesDir(null), "${photo.value?.id}.jpeg")
            try {
                if (file.exists()) {
                    _errorMessage.value = "Photo exists"
                } else {
                    photo.value?.let {
                        downloadImage(it.src.large2x, file)
                        db.photoDao().insert(it)
                        _isBookmarked.value = true
                        _errorMessage.value = "Photo successfully saved to bookmarks"
                    }
                }
            } catch (e: IOException) {
                _errorMessage.value = "Error saving photo"
            }
        }
    }

    fun downloadPhoto() {
        viewModelScope.launch {
            val directory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val file = File(directory, "${photo.value?.id}.jpeg")
            try {
                photo.value?.let {
                    downloadImage(it.src.large2x, file)
                    _errorMessage.value = "Photo downloaded"
                }
            } catch (e: IOException) {
                _errorMessage.value = "Error downloading photo"
            }
        }
    }

    private suspend fun getPhotoFromDB(id: Int): Photo? {
        return db.photoDao().getById(id)
    }

    private fun downloadImage(url: String, file: File) {
        CoroutineScope(Dispatchers.IO).launch {
            val bytes = URL(url).readBytes()
            val outputStream = FileOutputStream(file)
            outputStream.use { it.write(bytes) }
        }
    }
}