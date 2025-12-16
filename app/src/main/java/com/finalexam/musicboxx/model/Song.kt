package com.finalexam.musicboxx.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val coverArtRes: Int,
    val durationInSeconds: Int
) : Parcelable
