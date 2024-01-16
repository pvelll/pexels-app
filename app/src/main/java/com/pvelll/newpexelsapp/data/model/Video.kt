package com.pvelll.newpexelsapp.data.model

data class Video(
    val page: Int,
    val per_page: Int,
    val total_results: Int,
    val url: String,
    val videos: List<VideoX>
)