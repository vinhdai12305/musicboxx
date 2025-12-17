package com.finalexam.musicboxx.albums

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongAdapter
import com.finalexam.musicboxx.model.Song

class AlbumDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_detail)

        val rvSongs = findViewById<RecyclerView>(R.id.rvSongs)

        val listSong = arrayListOf(
            Song("1", "Bài hát 1", "Ca sĩ A", R.drawable.rhym, 210),
            Song("2", "Bài hát 2", "Ca sĩ B", R.drawable.robber, 180),
            Song("3", "Bài hát 3", "Ca sĩ C", R.drawable.wean, 200)
        )

        rvSongs.layoutManager = LinearLayoutManager(this)
        rvSongs.adapter = SongAdapter(listSong) { }
    }
}
