package com.finalexam.musicboxx.player

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.Song

class PlaySongActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvArtist: TextView
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)

        // 1️⃣ Ánh xạ view
        tvTitle = findViewById(R.id.tvTitle)
        tvArtist = findViewById(R.id.tvArtist)
        btnBack = findViewById(R.id.btnBack)

        // 2️⃣ Nhận dữ liệu Song (AN TOÀN – KHÔNG DEPRECATED)
        val song = intent.getSerializableExtra("SONG_DATA") as? Song
        if (song != null) {
            bindSongUI(song)
        }

        // 3️⃣ Xử lý UI
        setupBack()
        setupPlayPause()
        setupNextPrev()
        setupLyricsToggle()
    }

    // ===============================
    // BIND DATA
    // ===============================
    private fun bindSongUI(song: Song) {
        tvTitle.text = song.title
        tvArtist.text = song.artist
    }

    // ===============================
    // UI INTERACTIONS
    // ===============================
    private fun setupBack() {
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupPlayPause() {
        // TODO: xử lý play / pause sau
    }

    private fun setupNextPrev() {
        // TODO: xử lý next / previous sau
    }

    private fun setupLyricsToggle() {
        // TODO: xử lý lyrics sau
    }
}
