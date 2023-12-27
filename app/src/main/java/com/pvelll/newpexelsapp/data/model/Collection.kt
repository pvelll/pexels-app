package com.pvelll.newpexelsapp.data.model

data class PhotoGallery(
    val description: Any,
    val id: String,
    val mediaCount: Int,
    val photosCount: Int,
    val `private`: Boolean,
    val title: String,
    val videosCount: Int
)