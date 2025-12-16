package com.finalexam.musicboxx.folders // Đảm bảo đúng package

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.albums.Song // Import model Song cũ
import com.finalexam.musicboxx.albums.SongAdapter // Import adapter cũ

class FolderDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_detail)

        // 1. Dữ liệu giả cho Folder (VD: Nhạc US-UK)
        val listSongs = listOf(
            Song("Levitating", "Dua Lipa"),
            Song("Save Your Tears", "The Weeknd"),
            Song("Peaches", "Justin Bieber"),
            Song("Kiss Me More", "Doja Cat"),
            Song("Bad Habits", "Ed Sheeran"),
            Song("Industry Baby", "Lil Nas X")
        )

        // 2. Setup RecyclerView (Tái sử dụng SongAdapter cũ)
        val rvSongs = findViewById<RecyclerView>(R.id.rvSongs)
        rvSongs.layoutManager = LinearLayoutManager(this)
        rvSongs.adapter = SongAdapter(listSongs)
    }
}