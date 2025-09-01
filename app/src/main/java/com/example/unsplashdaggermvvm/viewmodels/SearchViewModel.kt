package com.example.unsplashdaggermvvm.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplashdaggermvvm.model.ImagesResponse
import com.example.unsplashdaggermvvm.repository.UnsplashRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val unsplashRepository: UnsplashRepository, app: Application
): AndroidViewModel(app) {

    private val sharedPreference: SharedPreferences = app.applicationContext.getSharedPreferences(
        "last_query", Context.MODE_PRIVATE
    )
    private var currentResult: Flow<PagingData<ImagesResponse>>? = null

    fun searchImage(query: String): Flow<PagingData<ImagesResponse>> {
        val orderBy = "relevant"
        val newResult: Flow<PagingData<ImagesResponse>> =
            unsplashRepository.searchImage(query,orderBy).cachedIn(viewModelScope)
        currentResult = newResult
        setCurrentQuery(query)
        return newResult
    }

    private fun setCurrentQuery(query: String) {
        sharedPreference.edit().apply {
            putString("query",query)
            apply()
        }
    }

    fun currentQuery(): String? {
        return sharedPreference.getString("query",null)
    }
}