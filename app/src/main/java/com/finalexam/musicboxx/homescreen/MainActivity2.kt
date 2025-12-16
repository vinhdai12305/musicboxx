package com.finalexam.musicboxx.homescreen

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.finalexam.musicboxx.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Kích hoạt chế độ Edge-to-Edge để giao diện tràn ra sau thanh trạng thái
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Sử dụng layout chính (phiên bản có ViewPager2)
        setContentView(R.layout.layout_homescreen)

        // Áp dụng padding cho System Bars để nội dung không bị che khuất
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Chỉ áp dụng padding trên và dưới cho layout chính
            v.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }

        // --- BẮT ĐẦU CẤU HÌNH VIEWPAGER VÀ TABS ---

        // 1. Ánh xạ các View quan trọng
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val bottomNav: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // 2. Khởi tạo và gán Adapter (sử dụng ViewPagerAdapter đã tạo ở bước trước)
        val pagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = pagerAdapter

        // 3. Kết nối TabLayout với ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Suggested"
                1 -> "Songs"
                2 -> "Artists"
                3 -> "Albums"
                4 -> "Favorites"
                else -> null
            }
        }.attach()

        // 4. Xử lý sự kiện cho BottomNavigationView
        setupBottomNavigationView(bottomNav)
    }

    private fun setupBottomNavigationView(bottomNav: BottomNavigationView) {
        // Đặt sẵn listener để bạn có thể xử lý điều hướng sau này
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Đã ở màn hình Home
                    true
                }
                R.id.navigation_favorites, R.id.navigation_playlists, R.id.navigation_settings -> {
                    Toast.makeText(this, "${item.title} Clicked", Toast.LENGTH_SHORT).show()
                    // TODO: Xử lý chuyển màn hình hoặc hành động tương ứng
                    true
                }
                else -> false
            }
        }
    }
}
