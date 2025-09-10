package com.example.unsplashdaggermvvm.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplashdaggermvvm.api.UnsplashService
import com.example.unsplashdaggermvvm.data.ImagesResponse
import com.example.unsplashdaggermvvm.utils.STARTING_PAGE
import java.io.IOException

class SearchDataSource(
    private val unsplashService: UnsplashService,
    private val query: String,
    private val orderBy: String
): PagingSource<Int, ImagesResponse>() {
    override fun getRefreshKey(state: PagingState<Int, ImagesResponse>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImagesResponse> {
        val page = params.key ?: STARTING_PAGE
        return try {
            val data = unsplashService.searchImages(query,orderBy,page,params.loadSize)
            LoadResult.Page(
                data = data.results,
                prevKey = if(page == STARTING_PAGE) null else page - 1,
                nextKey = if(page >= data.totalPages) null else page + 1
            )
        } catch (throwable: Throwable) {
            var exception = throwable
            if(throwable is IOException) {
                exception = IOException("Please check internet connection")
            }
            LoadResult.Error(exception)
        }
    }
}