package com.finalexam.musicboxx.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicItem(
    val id: String,
    val title: String,
    val artist: String,
    val imageResource: Int,
) : Parcelable
