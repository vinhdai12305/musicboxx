package com.finalexam.musicboxx.folders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongAdapter
import com.finalexam.musicboxx.model.Song

class FolderDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_folder_detail)

        val rvSongs = findViewById<RecyclerView>(R.id.rvSongs)

        // ✅ Dữ liệu giả – đúng cấu trúc Song mới
        val listSongs = mutableListOf(
            Song("1", "Levitating", "Dua Lipa", R.drawable.mck, 203),
            Song("2", "Save Your Tears", "The Weeknd", R.drawable.mck, 215),
            Song("3", "Peaches", "Justin Bieber", R.drawable.mck, 198),
            Song("4", "Kiss Me More", "Doja Cat", R.drawable.mck, 210),
            Song("5", "Bad Habits", "Ed Sheeran", R.drawable.mck, 224),
            Song("6", "Industry Baby", "Lil Nas X", R.drawable.mck, 212)
        )

        rvSongs.layoutManager = LinearLayoutManager(this)
        rvSongs.adapter = SongAdapter(listSongs) { song ->
            // TODO: mở PlaySongActivity nếu cần
        }
    }
}
