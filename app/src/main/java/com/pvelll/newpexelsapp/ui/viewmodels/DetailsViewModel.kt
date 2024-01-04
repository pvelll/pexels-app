package com.pvelll.newpexelsapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pvelll.newpexelsapp.data.database.PhotoDatabase
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.data.network.NetworkConnectivityObserver
import com.pvelll.newpexelsapp.data.repository.PhotoByIdRepositoryImpl
import org.koin.java.KoinJavaComponent.inject

class DetailsViewModel(private val photoByIdRepository: PhotoByIdRepositoryImpl ) : ViewModel() {
    private val db by inject<PhotoDatabase>(PhotoDatabase::class.java)
    private val connectivityObserver : MutableLiveData<NetworkConnectivityObserver> = MutableLiveData()






    private suspend fun getPhoto(id: Int) : Photo{
        return if(connectivityObserver.value?.isConnected() == true)
            photoByIdRepository.getPhotoById(id)
        else db.photoDao().getById(id)!!
    }
}