package com.pvelll.newpexelsapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import com.pvelll.newpexelsapp.data.repository.DatabaseRepositoryImpl

class BookmarksViewModel(private val repository : DatabaseRepositoryImpl, private val application: Application) : ViewModel() {

}