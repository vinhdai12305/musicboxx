package com.finalexam.musicboxx.albums

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R

class AlbumDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_detail)

        // 1. Dữ liệu giả
        val listSongs = listOf(
            Song("Chẵn Tinh", "LYHAN"),
            Song("Vũ Trụ Song Song", "LYHAN"),
            Song("Rơi Tự Do", "LYHAN"),
            Song("Ảo Ảnh", "LYHAN"),
            Song("Giai Điệu Bí Ẩn", "LYHAN"),
            Song("Mưa Đêm", "LYHAN")
        )

        // 2. Setup RecyclerView
        val rvSongs = findViewById<RecyclerView>(R.id.rvSongs)
        rvSongs.layoutManager = LinearLayoutManager(this) // Xếp theo chiều dọc
        rvSongs.adapter = SongAdapter(listSongs)
    }
}