package com.example.unsplashdaggermvvm.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.unsplashdaggermvvm.api.UnsplashService
import com.example.unsplashdaggermvvm.data.datasource.ImagesDataSource
import com.example.unsplashdaggermvvm.data.datasource.SearchDataSource
import com.example.unsplashdaggermvvm.utils.LOAD_SIZE
import javax.inject.Inject

class UnsplashRepository @Inject constructor(private val unsplashService: UnsplashService) {
    fun getImage(orderBy: String) = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = LOAD_SIZE),
        pagingSourceFactory = {
            ImagesDataSource(unsplashService,orderBy)
        }
    ).flow

    fun searchImage(query: String, orderBy: String) = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = LOAD_SIZE),
        pagingSourceFactory = {
            SearchDataSource(unsplashService,query,orderBy)
        }
    ).flow

    suspend fun sendDownload(id: String) {
        unsplashService.downloadImage(id)
    }

}