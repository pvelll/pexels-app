package com.pvelll.newpexelsapp.domain.models

data class FeaturedCollectionResponse(
    val collections: List<Collection>,
    val next_page: String,
    val page: Int,
    val per_page: Int,
    val prev_page: String,
    val total_results: Int
)
