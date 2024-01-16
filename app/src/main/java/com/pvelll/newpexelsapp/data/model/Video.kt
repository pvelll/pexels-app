package com.pvelll.newpexelsapp.data.model

data class Video(
    val duration: Int,
    val height: Int,
    val id: Int,
    val image: String,
    val url: String,
    val user: User,
    val video_files: List<VideoFile>,
    val video_pictures: List<VideoPicture>,
    val width: Int
)