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
import com.google.firebase.firestore.AggregateSource
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
            .collection("album") // Đảm bảo tên collection trên Firebase là "album"
            .get()
            .addOnSuccessListener { documents ->
                albumList.clear()

                for (document in documents) {
                    try {
                        // 1. Convert dữ liệu
                        val album = document.toObject(Album::class.java)
                        // 2. Gán ID từ document vào object
                        album.id = document.id

                        // 3. Logic fix ảnh: Nếu album chưa có ảnh, thử lấy field khác
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

                // [MỚI] --- Gọi hàm đếm số lượng bài hát sau khi đã có list Album ---
                countSongsForAlbums()
            }
            .addOnFailureListener { exception ->
                Log.e("AlbumsFragment", "Lỗi tải danh sách album", exception)
                tvTotalAlbums.text = "0 albums"
            }
    }

    // [MỚI] Hàm đếm số bài hát cho từng Album
    private fun countSongsForAlbums() {
        val db = FirebaseFirestore.getInstance()

        // Duyệt qua từng album trong danh sách để đếm
        for ((index, album) in albumList.withIndex()) {

            // Dùng tính năng Count Aggregation của Firestore (đếm server-side cho nhanh)
            db.collection("songs")
                .whereEqualTo("album", album.name) // Tìm các bài hát có tên album trùng khớp
                .count()
                .get(AggregateSource.SERVER)
                .addOnSuccessListener { snapshot ->
                    // Cập nhật số lượng vào model
                    val count = snapshot.count
                    album.songCount = count

                    // Chỉ làm mới đúng item đó để giao diện hiện số lên
                    adapter.notifyItemChanged(index)
                }
                .addOnFailureListener {
                    Log.e("AlbumsFragment", "Lỗi đếm bài hát cho album ${album.name}", it)
                }
        }
    }
}