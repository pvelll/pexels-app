package com.pvelll.newpexelsapp.domain.models

import com.pvelll.newpexelsapp.data.model.Video

data class VideoResponse(
    val page: Int,
    val per_page: Int,
    val total_results: Int,
    val url: String,
    val videos: List<Video>
)