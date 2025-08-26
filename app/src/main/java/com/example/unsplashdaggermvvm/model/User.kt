package com.example.unsplashdaggermvvm.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String
): Parcelable
