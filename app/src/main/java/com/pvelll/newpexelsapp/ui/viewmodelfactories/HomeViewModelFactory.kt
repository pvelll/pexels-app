package com.pvelll.newpexelsapp.ui.viewmodelfactories


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.data.repository.CuratedPhotosRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotoGalleryRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotosRepositoryImpl


class HomeViewModelFactory(
private val photosRepository: PhotosRepositoryImpl,
private val photoGalleryRepository: PhotoGalleryRepositoryImpl,
private val curatedPhotosRepository: CuratedPhotosRepositoryImpl,
private val connectivityObserver: NetworkConnectivityObserver
) : ViewModelProvider.Factory{

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModelFactory(
            photosRepository,
            photoGalleryRepository,
            curatedPhotosRepository,
            connectivityObserver
        ) as T
    }
}