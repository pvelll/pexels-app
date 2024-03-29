package com.pvelll.newpexelsapp.data.api

import com.pvelll.newpexelsapp.domain.models.CuratedPhotosResponse
import com.pvelll.newpexelsapp.domain.models.PhotoGalleryResponse
import com.pvelll.newpexelsapp.data.model.Photo
import com.pvelll.newpexelsapp.domain.models.PhotosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface PexelApi {

    @GET("search")
    suspend fun getPhotos(
        @Query("query") query: String = "curated",
        @Query("per_page") perPage: Int = 80,
        @Query("page") page: Int = 1
    ): Response<PhotosResponse>


    @GET("collections/featured")
    suspend fun getPhotoGallery(
        @Query("per_page") perPage: Int = 7,
    ): Response<PhotoGalleryResponse>

    @GET("curated")
    suspend fun getCuratedPictures(
        @Query("per_page") perPage: Int = 30,
    ): Response<CuratedPhotosResponse>

    @GET("photos/{id}")
    suspend fun getPhotoById(
        @Path("id") id: Int,
    ): Response<Photo>

}
