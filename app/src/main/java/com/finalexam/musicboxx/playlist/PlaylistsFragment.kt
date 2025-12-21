package com.finalexam.musicboxx.playlist

import android.os.Bundle
import android.view.View
import android.widget.TextView // Thêm import này
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.PlaylistAdapter
import com.finalexam.musicboxx.data.model.Playlist
import com.google.firebase.firestore.FirebaseFirestore

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private lateinit var playlistAdapter: PlaylistAdapter
    private val playlistList = ArrayList<Playlist>()
    private lateinit var tvPlaylistCount: TextView // Khai báo biến đếm số lượng

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ánh xạ View
        tvPlaylistCount = view.findViewById(R.id.tvPlaylistCount) // Ánh xạ Text đếm
        val rvPlaylists = view.findViewById<RecyclerView>(R.id.rvPlaylists)

        view.findViewById<View>(R.id.ivSearch)?.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        // 2. Setup Adapter
        playlistAdapter = PlaylistAdapter(playlistList) { playlist ->
            // Xử lý khi click vào item playlist
            // Toast.makeText(context, "Clicked: ${playlist.name}", Toast.LENGTH_SHORT).show()
        }

        rvPlaylists.adapter = playlistAdapter
        rvPlaylists.layoutManager = LinearLayoutManager(context)

        // 3. Tải dữ liệu từ Firebase
        fetchPlaylists()
    }

    private fun fetchPlaylists() {
        val db = FirebaseFirestore.getInstance()
        db.collection("playlists")
            .get()
            .addOnSuccessListener { documents ->
                playlistList.clear() // Xóa dữ liệu cũ trước khi thêm mới

                // --- QUAN TRỌNG: Đã XÓA dòng thêm nút "Add New" giả ---

                if (!documents.isEmpty) {
                    val realData = documents.toObjects(Playlist::class.java)
                    playlistList.addAll(realData)
                }

                // Cập nhật số lượng Playlist hiển thị trên màn hình
                tvPlaylistCount.text = "${playlistList.size} playlists"

                playlistAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Nếu lỗi thì chỉ xóa list, không thêm item giả
                playlistList.clear()
                tvPlaylistCount.text = "0 playlists"
                playlistAdapter.notifyDataSetChanged()
            }
    }
}