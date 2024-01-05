package com.pvelll.newpexelsapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.data.repository.CuratedPhotosRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotoGalleryRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotosRepositoryImpl
import com.pvelll.newpexelsapp.domain.models.CuratedPhotosResponse
import com.pvelll.newpexelsapp.domain.models.PhotoGalleryResponse
import com.pvelll.newpexelsapp.domain.models.PhotosResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class HomeViewModel(
    private val photosRepository: PhotosRepositoryImpl,
    private val photoGalleryRepository: PhotoGalleryRepositoryImpl,
    private val curatedPhotosRepository: CuratedPhotosRepositoryImpl,
    private val connectivityObserver: NetworkConnectivityObserver
) : ViewModel() {
    var pictureList: MutableLiveData<PhotosResponse> = MutableLiveData()
    var galleryList: MutableLiveData<PhotoGalleryResponse> = MutableLiveData()
    var curatedPhotosList: MutableLiveData<CuratedPhotosResponse> = MutableLiveData()
    var currentQuery: MutableLiveData<String> = MutableLiveData()
    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    var loadingProgress: MutableLiveData<Int> = MutableLiveData(0)

    init {
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