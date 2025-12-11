package com.finalexam.musicboxx.model

data class ArtistItem(
    // Sử dụng 'val' để khai báo các thuộc tính (tương đương với final fields và public getters trong Java)
    val id: Int,
    val name: String,
    val imageResource: Int // Lưu trữ ID tài nguyên Drawable (R.drawable.xxx)
)