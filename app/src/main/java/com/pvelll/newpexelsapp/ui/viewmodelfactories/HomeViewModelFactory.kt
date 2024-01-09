package com.pvelll.newpexelsapp.ui.viewmodelfactories

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.data.repository.CuratedPhotosRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotoGalleryRepositoryImpl
import com.pvelll.newpexelsapp.data.repository.PhotosRepositoryImpl
import com.pvelll.newpexelsapp.ui.viewmodels.HomeViewModel
import org.koin.java.KoinJavaComponent.inject

//
//class HomeViewModelFactory(
//    private val api: PexelApi,
//    private val application: Application
//) : ViewModelProvider.Factory{
//    private val photosRepository = PhotosRepositoryImpl(api)
//    private val photoGalleryRepository = PhotoGalleryRepositoryImpl(api)
//    private val curatedPhotosRepository = CuratedPhotosRepositoryImpl(api)
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return HomeViewModel(
//            photosRepository,
//            photoGalleryRepository,
//            curatedPhotosRepository,
//            application
//        ) as T
//    }
//}