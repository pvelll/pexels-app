package com.pvelll.newpexelsapp.ui.viewmodels

import android.app.Application
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pvelll.newpexelsapp.data.database.PhotoDatabase
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.data.repository.DatabaseRepositoryImpl
import com.pvelll.newpexelsapp.ui.adapters.BookmarksRecyclerViewAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class BookmarksViewModel( private val application: Application) : AndroidViewModel(application) {
    private val repository : DatabaseRepositoryImpl
        get() = DatabaseRepositoryImpl()
    private var loading: MutableLiveData<Boolean> = MutableLiveData()
    var loadingProgress: MutableLiveData<Int> = MutableLiveData(0)
    var allPhotos: LiveData<List<Photo>> = MutableLiveData()
    private lateinit var pictureAdapter: BookmarksRecyclerViewAdapter

    init {
        CoroutineScope(Dispatchers.IO).launch {
            allPhotos = repository.getPhotos()
        }
    }

    private val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
        override fun onChange(selfChange: Boolean) {
            observePhotos(pictureAdapter)
        }
    }

    private fun observePhotos(adapter: BookmarksRecyclerViewAdapter) {
        allPhotos.observeForever { photos ->
            adapter.setPictureData(photos as ArrayList<Photo>, getApplication())
        }
    }

    init {
        val app = getApplication<Application>()
        app.contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            observer
        )
    }

    override fun onCleared() {
        super.onCleared()
        val app = getApplication<Application>()
        app.contentResolver.unregisterContentObserver(observer)
    }

    fun setPictureAdapter(adapter: BookmarksRecyclerViewAdapter) {
        pictureAdapter = adapter
        observePhotos(pictureAdapter)
    }
}