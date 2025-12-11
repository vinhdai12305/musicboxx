package com.finalexam.musicboxx.homescreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.WindowCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

import com.finalexam.musicboxx.R // Import R nếu nó chưa có
import com.finalexam.musicboxx.model.MusicItem
import com.finalexam.musicboxx.model.ArtistItem
import com.finalexam.musicboxx.adapter.MusicSquareAdapter
import com.finalexam.musicboxx.adapter.ArtistCircleAdapter

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🚨 SỬA LỖI GỌI HÀM EDGE-TO-EDGE:
        // Thay thế EdgeToEdge.enable(this) bằng lệnh WindowCompat gốc
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(R.layout.layout_homescreen)

        // Áp dụng Window Insets (Padding cho System Bars)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupBottomNavigationView()

        // --- SETUP RECYCLERVIEW ---
        setupRecentlyPlayed()
        setupArtists()
        setupMostPlayed()
    }

    private fun setupBottomNavigationView() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        // Bạn có thể thêm logic xử lý item selected tại đây nếu cần chuyển Activity
    }

    // --- CÁC HÀM THIẾT LẬP RECYCLERVIEW ---
    private fun setupRecentlyPlayed() {
        val recycler: RecyclerView = findViewById(R.id.recycler_recently_played)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = MusicSquareAdapter(createDummyMusicData())
        recycler.adapter = adapter
    }

    private fun setupArtists() {
        val recycler: RecyclerView = findViewById(R.id.recycler_artists)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ArtistCircleAdapter(createDummyArtistData())
        recycler.adapter = adapter
    }

    private fun setupMostPlayed() {
        val recycler: RecyclerView = findViewById(R.id.recycler_most_played)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = MusicSquareAdapter(createDummyMostPlayedData())
        recycler.adapter = adapter
    }

    // --- CÁC HÀM TẠO DỮ LIỆU GIẢ (DUMMY DATA) VÀ THAM CHIẾU HÌNH ẢNH ---
    private fun createDummyMusicData(): List<MusicItem> {
        val data = mutableListOf<MusicItem>()
        // Sử dụng cú pháp constructor của Kotlin
        data.add(MusicItem(1, "Không Thời Gian", "Dương Domic", R.drawable.khong_thoi_gian))
        data.add(MusicItem(2, "Đánh Đổi", "Obito", R.drawable.danh_doi))
        data.add(MusicItem(3, "Năm Ấy", "Đức Phúc", R.drawable.nam_ay))
        data.add(MusicItem(4, "Còn Gì Đẹp Hơn", "Nguyễn Hùng", R.drawable.con_gi_dep_hon))
        return data
    }

    private fun createDummyArtistData(): List<ArtistItem> {
        val data = mutableListOf<ArtistItem>()
        // Sử dụng cú pháp constructor của Kotlin
        data.add(ArtistItem(101, "Rhymastic", R.drawable.rhym))
        data.add(ArtistItem(102, "Bray", R.drawable.bray))
        data.add(ArtistItem(103, "Huslang Robber", R.drawable.robber))
        data.add(ArtistItem(104, "MCK", R.drawable.mck))
        return data
    }

    private fun createDummyMostPlayedData(): List<MusicItem> {
        val data = mutableListOf<MusicItem>()
        // Sử dụng cú pháp constructor của Kotlin
        data.add(MusicItem(201, "Ghé Qua", "Dick & PC & Tofu", R.drawable.ghe_qua))
        data.add(MusicItem(202, "Còn Gì Đẹp Hơn", "Nguyễn Hùng", R.drawable.con_gi_dep_hon))
        data.add(MusicItem(203, "Y6U", "Nghệ sĩ", R.drawable.y6u))
        data.add(MusicItem(204, "1000 Ánh Mắt", "Shiki", R.drawable.anhmat))
        return data
    }
}