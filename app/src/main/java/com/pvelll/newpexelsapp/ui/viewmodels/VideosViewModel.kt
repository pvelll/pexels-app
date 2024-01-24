package com.pvelll.newpexelsapp.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pvelll.newpexelsapp.data.api.PexelApi
import com.pvelll.newpexelsapp.data.repository.VideoRepositoryImpl
import com.pvelll.newpexelsapp.domain.models.VideoResponse
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class VideosViewModel : ViewModel() {
    private val api by inject<PexelApi>(PexelApi::class.java)
    private val videoRepo = VideoRepositoryImpl(api)

    var videosList : MutableLiveData<VideoResponse> = MutableLiveData()

    fun getVideos(query : String = "popular"){
        viewModelScope.launch{
            videosList.postValue(videoRepo.getVideos(query))
        }

    }
}