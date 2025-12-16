package com.finalexam.musicboxx.homescreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity này sẽ hiển thị danh sách các bài hát yêu thích của người dùng.
 */
class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Kết nối với file layout (bạn cần tạo file này)
        // setContentView(R.layout.activity_favorites)

        // 2. Thiết lập giao diện (ví dụ: đặt tiêu đề cho Toolbar)
        // supportActionBar?.title = "Favorites"

        // 3. Gọi hàm để lấy và hiển thị dữ liệu
        // setupRecyclerView()
        // loadFavoriteSongs()
    }

    // Bạn có thể thêm các hàm khác ở đây, ví dụ:
    /*
    private fun setupRecyclerView() {
        // Code khởi tạo RecyclerView để hiển thị danh sách bài hát
    }

    private fun loadFavoriteSongs() {
        // Code để lấy dữ liệu các bài hát yêu thích từ ViewModel hoặc Repository
    }
    */
}
