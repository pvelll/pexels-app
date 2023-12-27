package com.pvelll.newpexelsapp.ui.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pvelll.newpexelsapp.data.repository.PhotoByIdRepositoryImpl
import com.pvelll.newpexelsapp.ui.viewmodels.DetailsViewModel

class DetailsViewModelFactory(private val photoByIdRepository: PhotoByIdRepositoryImpl): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun<T: ViewModel > create(modelClass: Class<T>): T{
        return DetailsViewModel(
            photoByIdRepository
        ) as T
    }
}