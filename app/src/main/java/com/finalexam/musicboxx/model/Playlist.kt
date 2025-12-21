package com.finalexam.musicboxx.data.model

import com.google.firebase.firestore.PropertyName
import java.io.Serializable // <--- 1. BẮT BUỘC IMPORT

data class Playlist(
    val id: String = "",

    @get:PropertyName("name")
    val name: String = "",

    @get:PropertyName("imageUrl")
    val imageUrl: String = "",

    // --- THÊM DÒNG NÀY (Theo yêu cầu của bạn) ---
    @get:PropertyName("artist")
    val artist: String = "",
    // --------------------------------------------

    @get:PropertyName("songCount")
    val songCount: String = "0 songs"
) : Serializable // <--- 2. QUAN TRỌNG: Thêm cái này để không bị lỗi crash app khi chuyển màn hình