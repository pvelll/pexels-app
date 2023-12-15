package com.pvelll.newpexelsapp.data.api

import com.pvelll.newpexelsapp.domain.models.CuratedPhotosResponse
import com.pvelll.newpexelsapp.domain.models.FeaturedCollectionResponse
import com.pvelll.newpexelsapp.domain.models.Photo
import com.pvelll.newpexelsapp.domain.models.PhotosResponse
import retrofit2.Response
import retrofit2.http.GET
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
    suspend fun getFeaturedCollection(
        @Query("per_page") perPage: Int = 7,
    ): Response<FeaturedCollectionResponse>

    @GET("curated")
    suspend fun getCuratedPictures(
        @Query("per_page") perPage: Int = 30,
    ): Response<CuratedPhotosResponse>

    @GET("photos/{id}")
    suspend fun getPhotoById(
        @Path("id") id: Int,
    ): Response<Photo>

}
