package com.pvelll.newpexelsapp.domain.models

import com.pvelll.newpexelsapp.data.model.Photo

data class PhotosResponse(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val photos: List<Photo>,
    val total_results: Int
)
