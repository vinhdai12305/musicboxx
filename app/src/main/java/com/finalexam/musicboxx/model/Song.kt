package com.finalexam.musicboxx.data.model

data class Song(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val duration: Long = 0L,
    val albumArtUrl: String = "",
    val audioUrl: String = "",
    val downloadCount: Long = 0L
)