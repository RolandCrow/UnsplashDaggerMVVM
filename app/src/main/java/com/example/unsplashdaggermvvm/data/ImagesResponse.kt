package com.example.unsplashdaggermvvm.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImagesResponse(
    var id: String,
    var color: String,
    var likes: Int,
    var description: String,
    var urls: PictureUrl,
    var user: User
): Parcelable

@Parcelize
data class PictureUrl(var regular: String, var full: String): Parcelable

@Parcelize
data class User(var name: String): Parcelable