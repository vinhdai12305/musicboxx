package com.finalexam.musicboxx.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import Song

class HomeTabFragment : Fragment(R.layout.fragment_home_tab) {

    // 1. Khai báo Adapter
    // Dùng SongsAdapter cho bài hát (hình vuông)
    private lateinit var recentAdapter: SongsAdapter
    private lateinit var mostPlayedAdapter: SongsAdapter

    // Dùng ArtistsAdapter cho nghệ sĩ (hình tròn)
    private lateinit var artistsAdapter: ArtistsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 2. Ánh xạ 3 RecyclerView từ giao diện
        val rvRecentlyPlayed = view.findViewById<RecyclerView>(R.id.rvRecentlyPlayed)
        val rvArtists = view.findViewById<RecyclerView>(R.id.rvArtists)
        val rvMostPlayed = view.findViewById<RecyclerView>(R.id.rvMostPlayed)

        // 3. Xử lý sự kiện click
        val onSongClick: (Song) -> Unit = { song ->
            // Khi click vào bài hát -> Phát nhạc
            Toast.makeText(context, "Đang phát: ${song.title}", Toast.LENGTH_SHORT).show()
            (activity as? MainActivity)?.playMusic(song.audioUrl)
        }

        val onArtistClick: (Song) -> Unit = { song ->
            // Khi click vào Nghệ sĩ -> Tạm thời hiện thông báo (hoặc xử lý mở trang profile sau này)
            Toast.makeText(context, "Nghệ sĩ: ${song.artist}", Toast.LENGTH_SHORT).show()
        }

        // 4. Khởi tạo Adapter
        recentAdapter = SongsAdapter(ArrayList(), onSongClick)     // Hình vuông
        mostPlayedAdapter = SongsAdapter(ArrayList(), onSongClick) // Hình vuông

        // QUAN TRỌNG: Khởi tạo ArtistsAdapter để hiện hình tròn
        artistsAdapter = ArtistsAdapter(ArrayList(), onArtistClick)

        // 5. Cài đặt RecyclerView (Tất cả đều lướt ngang)
        setupHorizontalRecyclerView(rvRecentlyPlayed, recentAdapter)
        setupHorizontalRecyclerView(rvArtists, artistsAdapter)
        setupHorizontalRecyclerView(rvMostPlayed, mostPlayedAdapter)

        // 6. Tải dữ liệu từ Firebase
        fetchHomeData()
    }

    // Hàm phụ trợ giúp code gọn hơn
    private fun setupHorizontalRecyclerView(rv: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv.setHasFixedSize(true)
    }

    private fun fetchHomeData() {
        val db = FirebaseFirestore.getInstance()

        // Lấy dữ liệu bài hát
        db.collection("songs").limit(10).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val list = documents.toObjects(Song::class.java)

                    // Cập nhật dữ liệu cho từng mục
                    recentAdapter.updateData(list)

                    // Mục Artists: Cũng dùng list bài hát nhưng Adapter sẽ chỉ lấy ảnh và tên ca sĩ để hiện hình tròn
                    artistsAdapter.updateData(list)

                    // Mục Most Played: Đảo lộn danh sách cho khác biệt chút
                    mostPlayedAdapter.updateData(list.shuffled())
                }
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE", "Lỗi tải dữ liệu", e)
            }
    }
}