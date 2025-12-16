package com.finalexam.musicboxx.albums

data class Album(
    val id: Int,
    val title: String,      // Ví dụ: Tâm Linh
    val artist: String,     // Ví dụ: LYHAN
    val year: String,       // Ví dụ: 2025
    val songCount: String,  // Ví dụ: 8 songs
    val imageResId: Int     // Ảnh bìa Album
)