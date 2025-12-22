package com.finalexam.musicboxx.hometab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.AlbumsAdapter
import com.finalexam.musicboxx.model.Album
import com.google.firebase.firestore.FirebaseFirestore

class AlbumsFragment : Fragment(R.layout.fragment_albums) {

    private lateinit var adapter: AlbumsAdapter
    private val albumList = mutableListOf<Album>()
    private lateinit var tvTotalAlbums: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAlbums = view.findViewById<RecyclerView>(R.id.rvAlbums)
        tvTotalAlbums = view.findViewById(R.id.tvTotalAlbums)

        // Adapter + click album
        adapter = AlbumsAdapter(albumList) { clickedAlbum ->
            openAlbumDetail(clickedAlbum)
        }

        rvAlbums.layoutManager = GridLayoutManager(requireContext(), 2)
        rvAlbums.adapter = adapter

        fetchAlbumsFromFirebase()
    }

    private fun openAlbumDetail(album: Album) {
        // Kiểm tra an toàn trước khi chuyển màn hình
        if (album.name.isEmpty()) return

        val bundle = Bundle().apply {
            putSerializable("album_data", album)
        }

        requireActivity()
            .findNavController(R.id.nav_host_fragment)
            .navigate(R.id.albumDetailFragment, bundle)
    }

    private fun fetchAlbumsFromFirebase() {
        FirebaseFirestore.getInstance()
            .collection("album") // Đảm bảo tên collection trên Firebase là "album" (số ít hay nhiều?)
            .get()
            .addOnSuccessListener { documents ->
                albumList.clear()

                for (document in documents) {
                    try {
                        // 1. Convert dữ liệu
                        val album = document.toObject(Album::class.java)
                        // 2. QUAN TRỌNG: Gán ID từ document vào object
                        album.id = document.id

                        // 3. Logic fix ảnh: Nếu album chưa có ảnh, thử lấy field khác (phòng hờ)
                        if (album.imageUrl.isEmpty() && document.contains("imageUrl")) {
                            album.imageUrl = document.getString("imageUrl") ?: ""
                        }

                        albumList.add(album)
                    } catch (e: Exception) {
                        Log.e("AlbumsFragment", "Lỗi convert album: ${document.id}", e)
                    }
                }

                tvTotalAlbums.text = "${albumList.size} albums"
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("AlbumsFragment", "Lỗi tải danh sách album", exception)
                tvTotalAlbums.text = "0 albums"
            }
    }
}