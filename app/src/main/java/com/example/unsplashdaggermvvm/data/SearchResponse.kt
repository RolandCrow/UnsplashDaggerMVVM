package com.example.unsplashdaggermvvm.data

data class SearchResponse(
    val results: List<ImagesResponse>,
    val total: Int,
    val totalPages: Int
)
