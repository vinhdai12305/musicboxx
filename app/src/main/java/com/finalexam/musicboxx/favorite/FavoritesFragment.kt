package com.finalexam.musicboxx.favorite

import Song
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongsListAdapter // Dùng lại Adapter này để có nút 3 chấm
import com.finalexam.musicboxx.bottomsheet.SongOptionListener
import com.finalexam.musicboxx.bottomsheet.SongOptionsBottomSheet
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    // Dùng SongsListAdapter để đồng bộ giao diện với màn hình Home
    private lateinit var adapter: SongsListAdapter
    private val favoriteList = mutableListOf<Song>()
    private var tvCount: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- 1. XỬ LÝ NÚT SEARCH (LOGIC CŨ GIỮ NGUYÊN) ---
        view.findViewById<View>(R.id.ivSearch)?.setOnClickListener {
            // Kiểm tra xem action này có tồn tại trong nav_graph không trước khi gọi để tránh crash
            try {
                findNavController().navigate(R.id.searchFragment)
            } catch (e: Exception) {
                Toast.makeText(context, "Chưa định nghĩa action searchFragment", Toast.LENGTH_SHORT).show()
            }
        }
        // --------------------------------------------------

        val rvFavorites = view.findViewById<RecyclerView>(R.id.rvFavorites)
        tvCount = view.findViewById<TextView>(R.id.tvCount)

        // 2. Setup Adapter
        // Sử dụng SongsListAdapter để có logic click bài hát và click 3 chấm
        adapter = SongsListAdapter(favoriteList,
            onSongClick = { song ->
                // Logic phát nhạc cũ
                if (song.audioUrl.isNotEmpty()) {
                    (activity as? MainActivity)?.playMusic(song.audioUrl)
                }
            },
            onMoreClick = { song ->
                // Hiện BottomSheet khi bấm 3 chấm
                showBottomSheet(song)
            }
        )

        rvFavorites.adapter = adapter
        rvFavorites.layoutManager = LinearLayoutManager(context)
    }

    // Dùng onResume để mỗi khi quay lại màn hình này thì load lại danh sách mới nhất
    override fun onResume() {
        super.onResume()
        fetchFavoriteSongs()
    }

    private fun fetchFavoriteSongs() {
        val db = FirebaseFirestore.getInstance()

        // 3. Tải dữ liệu từ collection "favorites" (Thay vì limit 20 bài random)
        db.collection("favorites")
            .get()
            .addOnSuccessListener { documents ->
                favoriteList.clear()
                if (!documents.isEmpty) {
                    for (document in documents) {
                        val song = document.toObject(Song::class.java)
                        favoriteList.add(song)
                    }
                }

                // Cập nhật Adapter
                adapter.notifyDataSetChanged()

                // Cập nhật text số lượng (Logic cũ)
                tvCount?.text = "${favoriteList.size} favorites"
            }
            .addOnFailureListener {
                Toast.makeText(context, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
    }

    // Hàm hiện BottomSheet (Xử lý xóa khỏi yêu thích ngay lập tức)
    private fun showBottomSheet(song: Song) {
        val bottomSheet = SongOptionsBottomSheet(song, object : SongOptionListener {

            override fun onFavoriteClick(song: Song, isFavorite: Boolean) {
                val db = FirebaseFirestore.getInstance()
                val songId = song.id ?: song.title

                // Ở màn hình Favorites, nếu bấm Tim lần nữa nghĩa là BỎ THÍCH
                if (!isFavorite) {
                    db.collection("favorites").document(songId).delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()

                            // Xóa khỏi list hiển thị và cập nhật UI ngay lập tức
                            favoriteList.remove(song)
                            adapter.notifyDataSetChanged()
                            tvCount?.text = "${favoriteList.size} favorites"
                        }
                }
            }

            // Các chức năng khác giữ nguyên
            override fun onPlayNext(song: Song) { Toast.makeText(context, "Play Next", Toast.LENGTH_SHORT).show() }
            override fun onAddToQueue(song: Song) { Toast.makeText(context, "Added to Queue", Toast.LENGTH_SHORT).show() }
            override fun onAddToPlaylist(song: Song) {}
            override fun onGoToAlbum(song: Song) {}
            override fun onGoToArtist(song: Song) {}
            override fun onDeleteSong(song: Song) {}
        })

        bottomSheet.show(parentFragmentManager, "FavBottomSheet")
    }
}