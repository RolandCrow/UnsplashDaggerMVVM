package com.example.unsplashdaggermvvm.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.unsplashdaggermvvm.api.UnsplashService
import com.example.unsplashdaggermvvm.data.ImagesDataSource
import com.example.unsplashdaggermvvm.data.SearchDataSource
import javax.inject.Inject

class UnsplashRepository @Inject constructor(private val unsplashService: UnsplashService) {

    fun getImages(orderBy: String) = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = 25),
        pagingSourceFactory = {
            ImagesDataSource(unsplashService,orderBy)
        }
    ).flow

    fun searchImage(query: String,orderBy: String) = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = 25),
        pagingSourceFactory = {
            SearchDataSource(unsplashService,query,orderBy)
        }
    ).flow

    suspend fun sendDownload(photoId: String) {
        unsplashService.downloadImage(photoId)
    }
}