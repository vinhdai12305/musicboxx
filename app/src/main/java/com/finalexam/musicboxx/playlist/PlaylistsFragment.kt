package com.finalexam.musicboxx.playlist

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.PlaylistAdapter
import com.finalexam.musicboxx.data.model.Playlist
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private lateinit var playlistAdapter: PlaylistAdapter
    private val playlistList = ArrayList<Playlist>() // List gốc để lưu trữ

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvPlaylists = view.findViewById<RecyclerView>(R.id.rvPlaylists)

        // 1. Setup Adapter
        playlistAdapter = PlaylistAdapter(playlistList) { playlist ->
            if (playlist.id == "ADD_NEW") {
                // Xử lý khi bấm nút Thêm mới
                Toast.makeText(context, "Mở màn hình tạo Playlist", Toast.LENGTH_SHORT).show()
            } else {
                // Xử lý khi bấm vào Playlist thật
                Toast.makeText(context, "Mở playlist: ${playlist.name}", Toast.LENGTH_SHORT).show()
            }
        }

        rvPlaylists.adapter = playlistAdapter
        rvPlaylists.layoutManager = LinearLayoutManager(context)

        // 2. Tải dữ liệu thật
        fetchPlaylists()
    }

    private fun fetchPlaylists() {
        val db = FirebaseFirestore.getInstance()

        // Giả sử tên collection trên Firebase là "playlists"
        db.collection("playlists")
            .get()
            .addOnSuccessListener { documents ->
                // 1. Tạo list tạm thời
                val tempList = ArrayList<Playlist>()

                // 2. LUÔN LUÔN thêm nút "Add New" vào đầu tiên

                if (!documents.isEmpty) {
                    // 3. Convert dữ liệu Firebase và thêm vào sau
                    val realData = documents.toObjects(Playlist::class.java)
                    tempList.addAll(realData)
                }

                // 4. Cập nhật Adapter
                playlistAdapter.updateData(tempList)

                // Cập nhật số lượng playlist lên giao diện (nếu có TextView hiển thị count)
                // updatePlaylistCount(tempList.size - 1)
            }
            .addOnFailureListener { e ->
                Log.e("FIREBASE", "Lỗi tải playlist", e)
                // Nếu lỗi mạng, vẫn hiện nút Add New
                val errorList = ArrayList<Playlist>()
                errorList.add(Playlist(id = "ADD_NEW", name = "Add New Playlist"))
                playlistAdapter.updateData(errorList)
            }
    }
}