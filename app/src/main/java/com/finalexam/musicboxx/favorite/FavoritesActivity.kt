package com.aicloudflare.musicbox.favorite

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.data.model.Song

class FavoritesActivity : AppCompatActivity() {

    private lateinit var rvSongs: RecyclerView
    private lateinit var adapter: FavoritesAdapter
    private val favoriteSongs = ArrayList<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite) // Nạp file XML màn hình chính

        // 1. Ánh xạ View
        rvSongs = findViewById(R.id.rvSongs)

        // Nút back (Header icon) - Tùy logic bạn muốn bấm vào logo làm gì
        findViewById<ImageView>(R.id.imgHeaderIcon).setOnClickListener {
            finish()
        }

        // 2. Tạo dữ liệu giả để test giao diện
        createDummyData()

        // 3. Cài đặt RecyclerView
        adapter = FavoritesAdapter(favoriteSongs)
        rvSongs.layoutManager = LinearLayoutManager(this)
        rvSongs.adapter = adapter
    }

    private fun createDummyData() {
        favoriteSongs.add(Song(title = "Không Buông", artist = "Hngle, Ari"))
        favoriteSongs.add(Song(title = "Người Đầu Tiên", artist = "Juky San"))
        favoriteSongs.add(Song(title = "Thanh Xuân", artist = "Da LAB"))
        favoriteSongs.add(Song(title = "Mộng Yu", artist = "AMEE, RPT MCK"))
        favoriteSongs.add(Song(title = "Bình Yên", artist = "Vũ., Binz"))
        favoriteSongs.add(Song(title = "Tầng Thượng 102", artist = "Cá Hồi Hoang"))
    }
}