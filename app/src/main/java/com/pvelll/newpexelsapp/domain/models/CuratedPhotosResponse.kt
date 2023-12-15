package com.pvelll.newpexelsapp.domain.models

data class CuratedPhotosResponse(
    val next_page: String,
    val per_page: Int,
    val page: Int,
    val photos: List<Photo>,
    val total_results: Int
)
