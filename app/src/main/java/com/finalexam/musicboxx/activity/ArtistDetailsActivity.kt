package com.finalexam.musicboxx.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.finalexam.musicboxx.R

class ArtistDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Gán layout XML cho Activity này
        setContentView(R.layout.activity_artist_details)

        // Thiết lập Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Kích hoạt nút quay lại (Back) trên Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Ẩn tiêu đề mặc định

        // Xử lý sự kiện khi nhấn nút quay lại
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Quay lại màn hình trước đó
        }

        // Tại đây, bạn sẽ lấy ID của nghệ sĩ được truyền qua Intent
        // val artistId = intent.getStringExtra("ARTIST_ID")
        // và dùng ID đó để fetch dữ liệu chi tiết từ Firebase/API và hiển thị lên giao diện.
    }
}
