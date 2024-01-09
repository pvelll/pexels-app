package com.pvelll.newpexelsapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.data.repository.CuratedPhotosRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotoGalleryRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotosRepositoryImpl
import com.pvelll.newpexelsapp.domain.models.CuratedPhotosResponse
import com.pvelll.newpexelsapp.domain.models.PhotoGalleryResponse
import com.pvelll.newpexelsapp.domain.models.PhotosResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import retrofit2.Response

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val api by KoinJavaComponent.inject<PexelApi>(PexelApi::class.java)
    private val connectivityObserver by KoinJavaComponent.inject<NetworkConnectivityObserver>(
        NetworkConnectivityObserver::class.java
    )
    private val photosRepository: PhotosRepositoryImpl = PhotosRepositoryImpl(api)
    private val photoGalleryRepository: PhotoGalleryRepositoryImpl = PhotoGalleryRepositoryImpl(api)
    private val curatedPhotosRepository: CuratedPhotosRepositoryImpl = CuratedPhotosRepositoryImpl(api)
    var pictureList: MutableLiveData<PhotosResponse> = MutableLiveData()
    var galleryList: MutableLiveData<PhotoGalleryResponse> = MutableLiveData()
    var curatedPhotosList: MutableLiveData<CuratedPhotosResponse> = MutableLiveData()
    var currentQuery: MutableLiveData<String> = MutableLiveData()
    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    var loadingProgress: MutableLiveData<Int> = MutableLiveData(0)

    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean>
        get() = _isNetworkAvailable
    init {
        _isNetworkAvailable.value = connectivityObserver.isConnected()
        getCuratedPhotos()
        getGalleries()
    }



    fun getPicture(query: String) {
        viewModelScope.launch {
            if (connectivityObserver.isConnected()) {
                loading.value = true
                setLadingProgress(0)
                val response = photosRepository.getPhotos(query)
                pictureList.postValue(response)
                imitateLoading()
                loading.value = false
                setLadingProgress(100)
            } else {
                Log.d("myLogs", "ошибка в получении фотки")
            }
        }
    }


    fun getCuratedPhotos() {
        viewModelScope.launch {
            if (connectivityObserver.isConnected()) {
                loading.value = true
                setLadingProgress(0)
                val response = curatedPhotosRepository.getCuratedPhotos()
                curatedPhotosList.postValue(response)
                imitateLoading()
                loading.value = false
                setLadingProgress(100)
            } else {
                Log.d("myLogs", "ошибка в получении фотки")
            }
        }
    }
    private suspend fun imitateLoading() {
        val startValue = 0
        val endValue = 100
        val duration = 1000
        val interval = 20

        val steps = duration / interval
        val stepValue = (endValue - startValue) / steps

        var currentValue = startValue

        repeat(steps.toInt()) {
            currentValue += stepValue
            if (currentValue > endValue) {
                currentValue = endValue
            }
            setLadingProgress(currentValue)
            delay(5)
        }
    }
    fun getGalleries() {
        viewModelScope.launch {
            if (connectivityObserver.isConnected()) {
                val response = photoGalleryRepository.getPhotoGallery()
                galleryList.postValue(response)
            } else {
                Log.d("myLogs", "ошибка в получении фотки")
            }
        }
    }
    private fun setLadingProgress(progress: Int) {
        loadingProgress.value = progress
    }
}