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
import com.pvelll.newpexelsapp.domain.connectivity.ConnectivityObserver
import com.pvelll.newpexelsapp.domain.models.CuratedPhotosResponse
import com.pvelll.newpexelsapp.domain.models.PhotoGalleryResponse
import com.pvelll.newpexelsapp.domain.models.PhotosResponse
import com.pvelll.newpexelsapp.domain.usecases.LoadStatus
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import retrofit2.Response

class HomeViewModel(
    application: Application,
) : AndroidViewModel(application)
{
    private val api by KoinJavaComponent.inject<PexelApi>(PexelApi::class.java)
    private val connectivityObserver by KoinJavaComponent.inject<NetworkConnectivityObserver>(
        NetworkConnectivityObserver::class.java
    )
    private val photosRepository: PhotosRepositoryImpl = PhotosRepositoryImpl(api)
    private val photoGalleryRepository: PhotoGalleryRepositoryImpl = PhotoGalleryRepositoryImpl(api)
    private val curatedPhotosRepository: CuratedPhotosRepositoryImpl =
        CuratedPhotosRepositoryImpl(api)
    private var _pictureList: MutableLiveData<PhotosResponse> = MutableLiveData()
    val pictureList: LiveData<PhotosResponse>
        get() = _pictureList
    private var _galleryList: MutableLiveData<PhotoGalleryResponse> = MutableLiveData()
    val galleryList: LiveData<PhotoGalleryResponse>
        get() = _galleryList
    private var _curatedPhotosList: MutableLiveData<CuratedPhotosResponse> = MutableLiveData()
    val curatedPhotosList: LiveData<CuratedPhotosResponse>
        get() = _curatedPhotosList
    private var _currentQuery: MutableLiveData<String> = MutableLiveData()
    val currentQuery: LiveData<String>
        get() = _currentQuery
    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    var loadingProgress: MutableLiveData<Int> = MutableLiveData(0)
    var curatedPhotosLoadStatus: MutableLiveData<LoadStatus> = MutableLiveData(LoadStatus.LOADING)

    private val _isNetworkAvailable = MutableLiveData<Boolean>()
    val isNetworkAvailable: LiveData<Boolean>
        get() = _isNetworkAvailable

    init {
        observeNetworkStatus()
        getCuratedPhotos()
        getGalleries()
    }

    fun getPicture(query: String) {
        viewModelScope.launch {
            if (connectivityObserver.isConnected()) {
                loading.value = true
                setLadingProgress(0)
                val response = photosRepository.getPhotos(query)
                _pictureList.postValue(response)
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
                try {
                    val response = curatedPhotosRepository.getCuratedPhotos()
                    _curatedPhotosList.postValue(response)
                    curatedPhotosLoadStatus.postValue(LoadStatus.SUCCESS)
                } catch (e: Exception) {
                    curatedPhotosLoadStatus.postValue(LoadStatus.FAILURE)
                }
                imitateLoading()
                loading.value = false
                setLadingProgress(100)
            } else {
                curatedPhotosLoadStatus.postValue(LoadStatus.NO_INTERNET)
                Log.d("myLogs", "ошибка в получении фотки")
            }
        }
    }

    private suspend fun imitateLoading() {
        viewModelScope.launch {
            val startValue = 0
            val endValue = 100
            val duration = 1000
            val interval = 20
            val steps = duration / interval
            val stepValue = (endValue - startValue) / steps
            var currentValue = startValue
            repeat(steps) {
                currentValue += stepValue
                if (currentValue > endValue) {
                    currentValue = endValue
                }
                setLadingProgress(currentValue)
                delay(5)
            }
        }
    }

    fun getGalleries() {
        viewModelScope.launch {
            if (connectivityObserver.isConnected()) {
                val response = photoGalleryRepository.getPhotoGallery()
                _galleryList.postValue(response)
            } else {
                Log.d("myLogs", "ошибка в получении фотки")
            }
        }
    }

    private fun observeNetworkStatus() {
        viewModelScope.launch {
            connectivityObserver.observe().collect { status ->
                _isNetworkAvailable.value = status == ConnectivityObserver.Status.AVAILABLE
            }
        }
    }

    private fun setLadingProgress(progress: Int) {
        viewModelScope.launch {
            loadingProgress.value = progress
        }
    }

    fun searchByTitle(title: String) {
        viewModelScope.launch {
            _currentQuery.value = title
            getPicture(title)
        }
    }

    fun searchByQuery(query: String) {
        viewModelScope.launch {
            _currentQuery.value = query
            if (query.isEmpty()) {
                getCuratedPhotos()
            } else {
                getPicture(query)
                val gallery = _galleryList.value?.collections?.find { it.title.equals(query, ignoreCase = true) }

            }
        }
    }
    fun getCurrentQuery(): String{
        return _currentQuery.value ?: ""
    }
    fun refreshData() {
        viewModelScope.launch {
            val query = getCurrentQuery()
            if (query.isNotEmpty()) {
                getPicture(query)
            } else {
                getCuratedPhotos()
                getGalleries()
            }
        }
    }
}