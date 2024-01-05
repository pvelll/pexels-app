package com.pvelll.newpexelsapp.domain.usecases

import com.pvelll.newpexelsapp.data.model.Photo

public interface OnPhotoClickListener {
    fun onPhotoClick(photo: Photo)
}