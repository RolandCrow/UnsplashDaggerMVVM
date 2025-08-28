package com.example.unsplashdaggermvvm.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplashdaggermvvm.model.ImagesResponse
import com.example.unsplashdaggermvvm.repository.UnsplashRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ImagesListViewModel @Inject constructor(private val unsplashRepository: UnsplashRepository): ViewModel() {
    private var currentResult: Flow<PagingData<ImagesResponse>>? = null

    fun getImage(): Flow<PagingData<ImagesResponse>> {
        val orderBy = listOf("latest","oldest","popular").random()
        val newResult: Flow<PagingData<ImagesResponse>> =
            unsplashRepository.getImages(orderBy).cachedIn(viewModelScope)
        return newResult
    }
}