package com.finalexam.musicboxx.homescreen

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.finalexam.musicboxx.R

class SongsActivity : AppCompatActivity() {

    // Khai báo các biến cho view để dễ dàng truy cập
    private lateinit var tabLayout: TabLayout
    private lateinit var searchIcon: ImageView
    private lateinit var sortButton: TextView
    private lateinit var songsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Kết nối layout XML với Activity
        setContentView(R.layout.activity_songs_list)

        // 2. Ánh xạ các view từ layout
        tabLayout = findViewById(R.id.tab_layout)
        searchIcon = findViewById(R.id.ic_search)
        sortButton = findViewById(R.id.sort_button)
        songsRecyclerView = findViewById(R.id.songs_recycler_view)

        // 3. THIẾT LẬP TRẠNG THÁI MẶC ĐỊNH CHO MÀN HÌNH
        setupDefaultState()

        // 4. CÀI ĐẶT CÁC SỰ KIỆN CLICK (LISTENER)
        setupClickListeners()

        // 5. CÀI ĐẶT RECYCLERVIEW
        setupRecyclerView()
    }

    private fun setupDefaultState() {
        // =================================================================
        // == ĐÂY LÀ PHẦN QUAN TRỌNG NHẤT ĐỂ CHỌN TAB "SONGS" MẶC ĐỊNH ==
        // =================================================================
        // Vị trí các tab: 0: Suggested, 1: Songs, 2: Artists...
        val songsTab = tabLayout.getTabAt(1)
        songsTab?.select()
        // Hoặc bạn cũng có thể dùng: tabLayout.selectTab(songsTab)
    }

    private fun setupClickListeners() {
        // Sự kiện click cho icon tìm kiếm
        searchIcon.setOnClickListener {
            // Tạm thời hiển thị một thông báo ngắn (Toast)
            Toast.makeText(this, "Search clicked!", Toast.LENGTH_SHORT).show()
            // TODO: Thêm logic mở màn hình tìm kiếm ở đây
        }

        // Sự kiện click cho nút sắp xếp
        sortButton.setOnClickListener {
            // Tạm thời hiển thị một thông báo ngắn (Toast)
            Toast.makeText(this, "Sort clicked!", Toast.LENGTH_SHORT).show()
            // TODO: Thêm logic hiển thị dialog sắp xếp (dialog_sort_options.xml) ở đây
        }

        // Sự kiện khi người dùng chọn một tab khác
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // TODO: Thêm logic để tải dữ liệu tương ứng với tab được chọn (ví dụ: Songs, Artists, Albums...)
                val tabName = tab?.text.toString()
                Toast.makeText(this@SongsActivity, "$tabName selected", Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Không cần xử lý
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Không cần xử lý
            }
        })
    }

    private fun setupRecyclerView() {
        // Thiết lập LayoutManager cho RecyclerView
        songsRecyclerView.layoutManager = LinearLayoutManager(this)

        // TODO: Khởi tạo Adapter của bạn và gán nó cho RecyclerView
        // Ví dụ:
        // val songList = loadSongs() // Hàm giả định để lấy danh sách bài hát
        // val songsAdapter = SongsAdapter(songList)
        // songsRecyclerView.adapter = songsAdapter
    }
}