package com.finalexam.musicboxx.albums

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AlbumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Đoạn code này sẽ nhúng AlbumFragment vào Activity để chạy luôn
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AlbumFragment())
                .commit()
        }
    }
}