package com.pvelll.newpexelsapp.ui.viewmodelfactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pvelll.newpexelsapp.data.repository.DatabaseRepositoryImpl
import com.pvelll.newpexelsapp.ui.viewmodels.BookmarksViewModel

class BookmarksViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun<T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BookmarksViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return BookmarksViewModel(application) as T
        }
        throw IllegalStateException("unknown ViewModel class $modelClass")
    }
}