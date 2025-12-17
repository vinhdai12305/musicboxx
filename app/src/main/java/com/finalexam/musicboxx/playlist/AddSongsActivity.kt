package com.finalexam.musicboxx.playlist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongAdapter
import com.finalexam.musicboxx.model.Song

class AddSongsActivity : AppCompatActivity() {

    private lateinit var rvSongs: RecyclerView
    private lateinit var adapter: SongAdapter
    private val songList = mutableListOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_songs)

        rvSongs = findViewById(R.id.rvSongs)

        setupDummyData()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = SongAdapter(songList) { song ->
            // TODO: xử lý thêm bài hát vào playlist
        }

        rvSongs.layoutManager = LinearLayoutManager(this)
        rvSongs.adapter = adapter
    }

    private fun setupDummyData() {
        songList.clear()

        songList.add(
            Song(
                id = "1",
                title = "Không Buông",
                artist = "Hngle Ari",
                coverArtRes = R.drawable.ic_launcher_foreground,
                durationInSeconds = 215
            )
        )

        songList.add(
            Song(
                id = "2",
                title = "Em Của Ngày Hôm Qua",
                artist = "Sơn Tùng M-TP",
                coverArtRes = R.drawable.ic_launcher_foreground,
                durationInSeconds = 240
            )
        )

        songList.add(
            Song(
                id = "3",
                title = "Waiting For You",
                artist = "Mono",
                coverArtRes = R.drawable.ic_launcher_foreground,
                durationInSeconds = 230
            )
        )
    }
}
