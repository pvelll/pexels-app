package com.pvelll.newpexelsapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.data.repository.CuratedPhotosRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotoGalleryRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotosRepositoryImpl

class HomeViewModel(
    private val photosRepository: PhotosRepositoryImpl,
    private val photoGalleryRepository: PhotoGalleryRepositoryImpl,
    private val curatedPhotosRepository: CuratedPhotosRepositoryImpl,
    private val connectivityObserver: NetworkConnectivityObserver
) : ViewModel() {
    // TODO: Implement the ViewModel
}