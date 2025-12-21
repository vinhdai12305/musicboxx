package com.finalexam.musicboxx.hometab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity // 1. Import MainActivity
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongsListAdapter
import Song // Import model Song
import com.google.firebase.firestore.FirebaseFirestore

class SongsFragment : Fragment(R.layout.fragment_songs) {

    private lateinit var adapter: SongsListAdapter
    private val songList = mutableListOf<Song>()
    private var tvTotalSongs: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvSongs = view.findViewById<RecyclerView>(R.id.rvSongs)
        tvTotalSongs = view.findViewById(R.id.tvTotalSongs)

        // 2. Setup Adapter
        adapter = SongsListAdapter(songList) { song ->
            // --- XỬ LÝ KHI CLICK BÀI HÁT ---

            // Kiểm tra link nhạc có trống không
            if (song.audioUrl.isNotEmpty()) {
                // Lấy Activity hiện tại và ép kiểu về MainActivity
                val mainActivity = activity as? MainActivity

                if (mainActivity != null) {
                    // Gọi hàm playMusic với tham số là String (URL)
                    // (Khớp với code MainActivity bạn vừa gửi)
                    mainActivity.playMusic(song.audioUrl)
                } else {
                    Log.e("SongsFragment", "Lỗi: Activity không phải MainActivity")
                }
            } else {
                Toast.makeText(context, "Bài hát này chưa có link nhạc", Toast.LENGTH_SHORT).show()
            }
        }

        rvSongs.layoutManager = LinearLayoutManager(context)
        rvSongs.adapter = adapter

        fetchSongsFromFirebase()
    }

    private fun fetchSongsFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("songs")
            .get()
            .addOnSuccessListener { documents ->
                songList.clear()
                for (document in documents) {
                    val song = document.toObject(Song::class.java)
                    songList.add(song)
                }

                // Cập nhật số lượng bài hát
                tvTotalSongs?.text = "${songList.size} songs"

                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("SongsFragment", "Lỗi tải dữ liệu: ", exception)
            }
    }
}