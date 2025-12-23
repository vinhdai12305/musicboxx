package com.finalexam.musicboxx.model
import com.google.firebase.firestore.Exclude
import java.io.Serializable

data class Album(
    var id: String = "",
    var name: String = "",
    var artist: String = "",
    var imageUrl: String = "",

    // Thêm dòng này (mặc định là 0)
    // @Exclude nghĩa là không bắt buộc field này phải có sẵn trên database, ta sẽ tính toán sau
    @get:Exclude
    var songCount: Long = 0
) : Serializable