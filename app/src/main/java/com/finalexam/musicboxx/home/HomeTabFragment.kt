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
// LƯU Ý: Import đúng class Song của bạn
// import com.finalexam.musicboxx.model.Song
import Song

class HomeTabFragment : Fragment(R.layout.fragment_home_tab) {

    private lateinit var songsAdapter: SongsAdapter
    // Khởi tạo list rỗng ban đầu
    private var songList: ArrayList<Song> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ánh xạ RecyclerView từ layout fragment_home_tab.xml
        // (Đảm bảo ID trong xml là rvRecentlyPlayed hoặc rvSongs tùy bạn đặt)
        val rvRecentlyPlayed = view.findViewById<RecyclerView>(R.id.rvRecentlyPlayed)

        // 2. Cài đặt Adapter và xử lý sự kiện CLICK
        songsAdapter = SongsAdapter(songList) { song ->
            // --- LOGIC KHI CLICK VÀO BÀI HÁT ---
            Toast.makeText(context, "Đang phát: ${song.title}", Toast.LENGTH_SHORT).show()

            // Gọi về MainActivity để phát nhạc
            val mainActivity = activity as? MainActivity
            if (mainActivity != null) {
                // Đảm bảo biến chứa link nhạc trong Song là audioUrl
                mainActivity.playMusic(song.audioUrl)
            } else {
                Log.e("HomeTabFragment", "Không tìm thấy MainActivity")
            }
        }

        // 3. Cấu hình RecyclerView
        rvRecentlyPlayed.adapter = songsAdapter
        // Horizontal: Lướt ngang (phù hợp mục Recently Played)
        // Nếu muốn danh sách dọc thì xóa chữ LinearLayoutManager.HORIZONTAL, false đi
        rvRecentlyPlayed.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // 4. Gọi hàm tải dữ liệu
        fetchSongsFromFirebase()
    }

    // Hàm lấy dữ liệu từ Firebase (Logic cũ của bạn, chạy rất ổn)
    private fun fetchSongsFromFirebase() {
        val db = FirebaseFirestore.getInstance()

        db.collection("songs")
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val listData = documents.toObjects(Song::class.java)

                    // Cập nhật dữ liệu vào Adapter
                    songsAdapter.updateData(listData)

                    Log.d("FIREBASE", "Đã tải: ${listData.size} bài hát")
                } else {
                    Log.d("FIREBASE", "Không có dữ liệu")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE", "Lỗi tải nhạc", e)
                Toast.makeText(context, "Lỗi kết nối!", Toast.LENGTH_SHORT).show()
            }
    }
}