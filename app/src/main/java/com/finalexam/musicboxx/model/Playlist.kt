package com.finalexam.musicboxx.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Playlist(
    var id: String = "",
    var name: String = "",

    @get:PropertyName("image_url")
    @set:PropertyName("image_url")
    var imageUrl: String = "",

    var songCount: Int = 0
)
