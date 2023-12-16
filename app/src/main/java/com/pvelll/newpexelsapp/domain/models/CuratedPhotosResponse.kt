package com.pvelll.newpexelsapp.domain.models

import com.pvelll.newpexelsapp.data.model.Photo

data class CuratedPhotosResponse(
    val next_page: String,
    val per_page: Int,
    val page: Int,
    val photos: List<Photo>,
    val total_results: Int
)
