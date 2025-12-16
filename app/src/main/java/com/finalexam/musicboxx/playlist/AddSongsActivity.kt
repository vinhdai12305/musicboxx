package com.aicloudflare.musicbox.playlist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aicloudflare.musicbox.playlist.AddSongsAdapter // Đảm bảo import đúng package Adapter
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.data.model.Song

class AddSongsActivity : AppCompatActivity() {
    // Khởi tạo các biến an toàn, không cần để null
    private lateinit var rvAddSongs: RecyclerView
    private lateinit var adapter: AddSongsAdapter

    // Khởi tạo list rỗng ngay từ đầu để tránh lỗi null
    private val songList = ArrayList<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_songs)

        // 1. Xử lý nút Back (Dùng cú pháp lambda gọn của Kotlin)
        findViewById<View>(R.id.btnBack).setOnClickListener { finish() }

        // 2. Khởi tạo RecyclerView
        rvAddSongs = findViewById(R.id.rvSongs)
        rvAddSongs.layoutManager = LinearLayoutManager(this)

        // 3. Tạo dữ liệu giả
        createDummyData()

        // 4. Gán Adapter (Truyền list đã có dữ liệu vào)
        adapter = AddSongsAdapter(songList)
        rvAddSongs.adapter = adapter
    }

    private fun createDummyData() {
        // Xóa dữ liệu cũ nếu có
        songList.clear()

        // Thêm dữ liệu mẫu (Lưu ý: Đảm bảo bạn có file ảnh ic_playlist_gray trong drawable)
        // Nếu chưa có, hãy đổi thành R.drawable.ic_launcher_background để test tạm
        songList.add(Song(title = "Không Buông", artist = "Hngle, Ari", albumArtUrl = "", id = "1").apply {
            // Nếu model Song của bạn dùng int coverResId thì sửa lại constructor Song cho khớp
            // Ở đây tôi giả định bạn đang dùng Model Song mới nhất bạn gửi (chỉ có albumArtUrl)
            // Nếu bạn muốn dùng ID ảnh drawable, hãy thêm field đó vào Model Song hoặc dùng logic tạm
        })

        // --- CHÚ Ý QUAN TRỌNG VỀ MODEL SONG ---
        // Class Song bạn gửi chỉ có `albumArtUrl: String`.
        // Nhưng Adapter bạn lại dùng `song.getCoverResId()`.
        // => Bạn cần cập nhật lại class Song hoặc Adapter.
        // Dưới đây tôi viết code giả định bạn ĐÃ SỬA Class Song có thêm field `coverResId: Int`.

        // Ví dụ code thêm bài hát (Giả sử bạn đã thêm field coverResId và isAdded vào Song):
        /*
        songList.add(Song("Không Buông", "Hngle, Ari", R.drawable.ic_playlist_gray, false))
        songList.add(Song("Người Đầu Tiên", "Juky San", R.drawable.ic_playlist_gray, false))
        songList.add(Song("Thanh Xuân", "Da LAB", R.drawable.ic_playlist_gray, true))
        songList.add(Song("Mộng Yu", "AMEE, RPT MCK", R.drawable.ic_playlist_gray, false))
        songList.add(Song("Bình Yên", "Vũ., Binz", R.drawable.ic_playlist_gray, true))
        */
    }
}