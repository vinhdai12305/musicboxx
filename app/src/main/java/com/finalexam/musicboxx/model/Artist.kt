package com.finalexam.musicboxx.model

import java.io.Serializable

// <--- BẮT BUỘC PHẢI CÓ DÒNG NÀY

data class Artist(
    val name: String = "",       // Tên field trên Firebase (vd: name, artistName...)
    val image: String = ""       // Tên field ảnh trên Firebase (vd: image, imageUrl...)
) : Serializable