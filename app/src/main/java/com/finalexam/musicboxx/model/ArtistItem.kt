package com.finalexam.musicboxx.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// THÊM @Parcelize VÀ IMPLEMENT Parcelable
@Parcelize
data class ArtistItem(
    val id: String,
    val name: String,
    val imageResource: Int,
    val albumCount: Int,
    val songCount: Int
) : Parcelable
