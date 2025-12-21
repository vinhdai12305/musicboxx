package com.finalexam.musicboxx.playlist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController // QUAN TRỌNG
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.PlaylistAdapter
import com.finalexam.musicboxx.data.model.Playlist // Kiểm tra lại package Playlist
import com.google.firebase.firestore.FirebaseFirestore

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private lateinit var playlistAdapter: PlaylistAdapter
    private val playlistList = ArrayList<Playlist>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- 1. XỬ LÝ NÚT SEARCH (MỚI THÊM) ---
        // Dùng findViewById<View> để an toàn, tránh lỗi null nếu ID chưa load kịp
        view.findViewById<View>(R.id.ivSearch)?.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        // --------------------------------------

        val rvPlaylists = view.findViewById<RecyclerView>(R.id.rvPlaylists)

        // 2. Setup Adapter
        playlistAdapter = PlaylistAdapter(playlistList) { playlist ->
            if (playlist.id == "ADD_NEW") {
                Toast.makeText(context, "Tạo Playlist mới", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Mở playlist: ${playlist.name}", Toast.LENGTH_SHORT).show()
            }
        }

        rvPlaylists.adapter = playlistAdapter
        rvPlaylists.layoutManager = LinearLayoutManager(context)

        // 3. Tải dữ liệu
        fetchPlaylists()
    }

    private fun fetchPlaylists() {
        val db = FirebaseFirestore.getInstance()
        db.collection("playlists")
            .get()
            .addOnSuccessListener { documents ->
                val tempList = ArrayList<Playlist>()

                // Luôn thêm nút Add New đầu tiên
                tempList.add(Playlist(id = "ADD_NEW", name = "Add New Playlist"))

                if (!documents.isEmpty) {
                    val realData = documents.toObjects(Playlist::class.java)
                    tempList.addAll(realData)
                }

                // Cập nhật Adapter (bạn cần viết hàm updateData trong Adapter hoặc dùng playlistList.clear/addAll)
                // Giả sử Adapter nhận list trực tiếp thì ta update list đó
                playlistList.clear()
                playlistList.addAll(tempList)
                playlistAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Nếu lỗi mạng, vẫn hiện nút Add New
                playlistList.clear()
                playlistList.add(Playlist(id = "ADD_NEW", name = "Add New Playlist"))
                playlistAdapter.notifyDataSetChanged()
            }
    }
}