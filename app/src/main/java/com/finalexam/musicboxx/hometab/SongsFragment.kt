package com.finalexam.musicboxx.hometab

import Song
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels // Import ViewModel chung
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongsListAdapter
import com.finalexam.musicboxx.bottomsheet.SongOptionListener // Import đúng package
import com.finalexam.musicboxx.bottomsheet.SongOptionsBottomSheet
import com.finalexam.musicboxx.home.HomeTabViewModel
import com.google.firebase.firestore.FirebaseFirestore

class SongsFragment : Fragment(R.layout.fragment_songs) {

    // Kết nối ViewModel chung để phát nhạc ExoPlayer
    private val viewModel: HomeTabViewModel by activityViewModels()

    private lateinit var adapter: SongsListAdapter
    private val songList = mutableListOf<Song>()
    private var tvTotalSongs: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvSongs = view.findViewById<RecyclerView>(R.id.rvSongs)
        tvTotalSongs = view.findViewById(R.id.tvTotalSongs)

        // Setup Adapter
        adapter = SongsListAdapter(
            songs = songList,
            onSongClick = { song ->
                // LOGIC PHÁT NHẠC: Gọi hàm playUserList của HomeTabViewModel
                val index = songList.indexOf(song)
                if (index != -1) {
                    viewModel.playUserList(songList, index)
                }
            },
            onMoreClick = { song ->
                // Hiện BottomSheet
                showBottomSheet(song)
            }
        )

        rvSongs.layoutManager = LinearLayoutManager(context)
        rvSongs.adapter = adapter

        fetchSongsFromFirebase()
    }

    private fun fetchSongsFromFirebase() {
        FirebaseFirestore.getInstance().collection("songs")
            .get()
            .addOnSuccessListener { documents ->
                songList.clear()
                for (document in documents) {
                    val song = document.toObject(Song::class.java)
                    // Lưu ID document để dùng cho chức năng xóa/thích
                    song.id = document.id
                    songList.add(song)
                }
                tvTotalSongs?.text = "${songList.size} songs"
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Lỗi tải bài hát", Toast.LENGTH_SHORT).show()
            }
    }

    // --- HÀM SHOW BOTTOM SHEET ĐÃ SỬA ---
    private fun showBottomSheet(song: Song) {
        // Tạo listener TRƯỚC
        val listener = object : SongOptionListener {
            override fun onFavoriteClick(song: Song, isFavorite: Boolean) {
                // Xử lý Thêm/Xóa Favorite vào Firebase
                val db = FirebaseFirestore.getInstance()
                val songId = song.id ?: song.title
                if (isFavorite) {
                    db.collection("favorites").document(songId).set(song)
                        .addOnSuccessListener { Toast.makeText(context, "Đã thích", Toast.LENGTH_SHORT).show() }
                } else {
                    db.collection("favorites").document(songId).delete()
                        .addOnSuccessListener { Toast.makeText(context, "Bỏ thích", Toast.LENGTH_SHORT).show() }
                }
            }

            override fun onPlayNext(song: Song) {
                // Logic Play Next (nếu cần)
                Toast.makeText(context, "Play Next: ${song.title}", Toast.LENGTH_SHORT).show()
            }

            override fun onDeleteSong(song: Song) {
                showDeleteDialog(song)
            }

            // Các hàm chưa dùng thì để trống
            override fun onAddToQueue(song: Song) {}
            override fun onAddToPlaylist(song: Song) {}
            override fun onGoToAlbum(song: Song) {}
            override fun onGoToArtist(song: Song) {}
        }

        // Truyền listener vào constructor
        val bottomSheet = SongOptionsBottomSheet(song, listener)
        bottomSheet.show(parentFragmentManager, "SongOptionsBottomSheet")
    }

    private fun showDeleteDialog(song: Song) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xóa bài hát?")
            .setMessage("Bạn có chắc chắn muốn xóa '${song.title}' vĩnh viễn không?")
            .setPositiveButton("Xóa") { _, _ ->
                val db = FirebaseFirestore.getInstance()
                val songId = song.id ?: song.title
                db.collection("songs").document(songId).delete()
                    .addOnSuccessListener {
                        songList.remove(song)
                        adapter.notifyDataSetChanged()
                        tvTotalSongs?.text = "${songList.size} songs"
                        Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}