package com.example.unsplashdaggermvvm.model

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
