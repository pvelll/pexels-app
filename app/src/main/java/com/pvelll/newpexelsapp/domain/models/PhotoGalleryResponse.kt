package com.pvelll.newpexelsapp.domain.models

import com.pvelll.newpexelsapp.data.model.PhotoGallery

data class FeaturedCollectionResponse(
    val photoGalleries: List<PhotoGallery>,
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val prev_page: String,
    val total_results: Int
)
