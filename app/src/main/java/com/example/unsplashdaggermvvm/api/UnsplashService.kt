package com.example.unsplashdaggermvvm.api

import com.example.unsplashdaggermvvm.data.ImagesResponse
import com.example.unsplashdaggermvvm.data.SearchResponse
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashService {
    @GET("photos")
    suspend fun getImages(
        @Query("order_by") orderBy: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): List<ImagesResponse>

    @GET("search/photos")
    suspend fun searchImages(
        @Query("query") query: String,
        @Query("order_by") orderBy: String,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): SearchResponse

    @GET("photos/{id}/download")
    suspend fun downloadImage(
        @Path("id")  photosId: String
    ): ResponseBody
}