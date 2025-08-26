package com.example.unsplashdaggermvvm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResponse(
    var results: List<ImagesResponse>,
    var total: Int,
    var totalPages: Int
): Parcelable