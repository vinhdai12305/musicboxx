package com.finalexam.musicboxx.data.model

import com.google.firebase.firestore.PropertyName

data class Playlist(
    val id: String = "",

    @get:PropertyName("name")
    val name: String = "",

    @get:PropertyName("imageUrl")
    val imageUrl: String = "",

    // --- THÊM DÒNG NÀY ---
    @get:PropertyName("artist")
    val artist: String = "",
    // ---------------------

    @get:PropertyName("songCount")
    val songCount: String = "0 songs"
)