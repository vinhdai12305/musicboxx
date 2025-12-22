package com.finalexam.musicboxx.model

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class Album(
    var id: String = "",
    var name: String = "",
    var artist: String = "",

    // Mapping 2 chiều: Đọc field "image_url" từ Firebase vào biến imageUrl
    @get:PropertyName("imageUrl")
    @set:PropertyName("imageUrl")
    var imageUrl: String = ""
) : Serializable