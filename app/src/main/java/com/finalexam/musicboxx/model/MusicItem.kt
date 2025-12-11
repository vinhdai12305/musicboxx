package com.finalexam.musicboxx.model

data class MusicItem(
    // ID của bài hát
    val id: Int,
    // Tên bài hát/album
    val title: String,
    // Tên nghệ sĩ
    val artist: String,
    // ID tài nguyên hình ảnh (R.drawable.xxx)
    val imageResource: Int
)