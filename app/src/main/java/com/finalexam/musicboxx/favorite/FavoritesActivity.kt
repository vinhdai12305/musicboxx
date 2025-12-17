package com.finalexam.musicboxx.favorite

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.Song
import com.finalexam.musicboxx.player.PlaySongActivity

class FavoritesActivity : AppCompatActivity() {

    private lateinit var rvSongs: RecyclerView
    private lateinit var adapter: FavoritesAdapter

    // Tạm thời dùng data giả
    private val favoriteSongs = mutableListOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        // RecyclerView
        rvSongs = findViewById(R.id.rvSongs)

        // Back icon
        findViewById<ImageView>(R.id.imgHeaderIcon).setOnClickListener {
            finish()
        }

        // Fake data để test UI
        createDummyData()

        // Adapter
        adapter = FavoritesAdapter(favoriteSongs) { song ->
            val intent = Intent(this, PlaySongActivity::class.java)
            intent.putExtra("SONG_DATA", song)
            startActivity(intent)
        }

        rvSongs.layoutManager = LinearLayoutManager(this)
        rvSongs.adapter = adapter
    }

    private fun createDummyData() {
        favoriteSongs.add(
            Song(
                id = "1",
                title = "Không Buông",
                artist = "Hngle, Ari",
                coverArtRes = R.drawable.rhym,
                durationInSeconds = 215
            )
        )

        favoriteSongs.add(
            Song(
                id = "2",
                title = "Wean",
                artist = "Wean Lê",
                coverArtRes = R.drawable.wean,
                durationInSeconds = 198
            )
        )

        favoriteSongs.add(
            Song(
                id = "3",
                title = "Tình",
                artist = "Y6u",
                coverArtRes = R.drawable.y6u,
                durationInSeconds = 240
            )
        )
    }
}
