package com.finalexam.musicboxx.favorite

import Song
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongsListAdapter
import com.finalexam.musicboxx.bottomsheet.SongOptionListener
import com.finalexam.musicboxx.bottomsheet.SongOptionsBottomSheet
import com.finalexam.musicboxx.home.HomeTabViewModel
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val viewModel: HomeTabViewModel by activityViewModels()
    private lateinit var adapter: SongsListAdapter
    private val favoriteList = mutableListOf<Song>()
    private var tvCount: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Setup Views
        val rvFavorites = view.findViewById<RecyclerView>(R.id.rvFavorites)
        tvCount = view.findViewById<TextView>(R.id.tvCount)

        view.findViewById<View>(R.id.ivSearch)?.setOnClickListener {
            // Code chuyển màn hình search (giữ nguyên của bạn)
        }

        // 2. Setup Adapter
        adapter = SongsListAdapter(
            songs = favoriteList,
            onSongClick = { song ->
                val index = favoriteList.indexOf(song)
                if (index != -1) {
                    viewModel.playUserList(favoriteList, index)
                }
            },
            onMoreClick = { song ->
                showBottomSheet(song)
            }
        )

        rvFavorites.adapter = adapter
        rvFavorites.layoutManager = LinearLayoutManager(context)

        // 3. Load Data
        fetchFavoriteSongs()
    }

    private fun fetchFavoriteSongs() {
        val db = FirebaseFirestore.getInstance()
        db.collection("favorites").get()
            .addOnSuccessListener { documents ->
                favoriteList.clear()
                for (document in documents) {
                    val song = document.toObject(Song::class.java)
                    song.id = document.id
                    favoriteList.add(song)
                }
                adapter.notifyDataSetChanged()
                tvCount?.text = "${favoriteList.size} favorites"
            }
            .addOnFailureListener {
                Toast.makeText(context, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
    }

    // --- SỬA LỖI TẠI ĐÂY ---
    private fun showBottomSheet(song: Song) {
        // Tạo đối tượng listener trước
        val listener = object : SongOptionListener {
            override fun onFavoriteClick(song: Song, isFavorite: Boolean) {
                if (!isFavorite) {
                    val db = FirebaseFirestore.getInstance()
                    val songId = song.id ?: song.title

                    db.collection("favorites").document(songId).delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show()
                            favoriteList.remove(song)
                            adapter.notifyDataSetChanged()
                            tvCount?.text = "${favoriteList.size} favorites"
                        }
                }
            }
            override fun onPlayNext(song: Song) {}
            override fun onAddToQueue(song: Song) {}
            override fun onAddToPlaylist(song: Song) {}
            override fun onGoToAlbum(song: Song) {}
            override fun onGoToArtist(song: Song) {}
            override fun onDeleteSong(song: Song) {}
        }

        // Truyền listener vào TRONG Constructor luôn
        // Lúc trước lỗi vì viết: SongOptionsBottomSheet(song) -> Thiếu tham số thứ 2
        val bottomSheet = SongOptionsBottomSheet(song, listener)

        bottomSheet.show(parentFragmentManager, "FavBottomSheet")
    }
}