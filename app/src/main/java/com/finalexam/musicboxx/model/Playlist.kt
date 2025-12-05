package com.finalexam.musicboxx.data.model

import com.google.firebase.Timestamp

data class Playlist(
    val id: String = "",
    val name: String = "",
    val userId: String = "",
    val songIds: List<String> = emptyList(),
    val coverUrl: String = "",
    val createdAt: Timestamp? = null
)