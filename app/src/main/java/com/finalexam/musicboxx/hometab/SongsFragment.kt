package com.finalexam.musicboxx.hometab

import Song
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongsListAdapter
import com.finalexam.musicboxx.bottomsheet.SongOptionListener
import com.finalexam.musicboxx.bottomsheet.SongOptionsBottomSheet
import com.google.firebase.firestore.FirebaseFirestore

class SongsFragment : Fragment(R.layout.fragment_songs) {

    private lateinit var adapter: SongsListAdapter
    private val songList = mutableListOf<Song>()
    private var tvTotalSongs: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvSongs = view.findViewById<RecyclerView>(R.id.rvSongs)
        tvTotalSongs = view.findViewById(R.id.tvTotalSongs)

        // 1. Setup Adapter
        adapter = SongsListAdapter(songList,
            onSongClick = { song ->
                // Logic phát nhạc
                if (song.audioUrl.isNotEmpty()) {
                    (activity as? MainActivity)?.playMusic(song.audioUrl)
                } else {
                    Toast.makeText(context, "Bài hát chưa có link nhạc", Toast.LENGTH_SHORT).show()
                }
            },
            onMoreClick = { song ->
                // Hiện BottomSheet
                showBottomSheet(song)
            }
        )

        rvSongs.layoutManager = LinearLayoutManager(context)
        rvSongs.adapter = adapter

        // 2. Tải dữ liệu
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
                    // Gán ID document vào object Song để sau này dễ xóa/sửa
                    song.id = document.id
                    songList.add(song)
                }

                tvTotalSongs?.text = "${songList.size} songs"
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.e("SongsFragment", "Lỗi tải dữ liệu", e)
            }
    }

    private fun showBottomSheet(song: Song) {
        val bottomSheet = SongOptionsBottomSheet(song, object : SongOptionListener {

            // --- XỬ LÝ TIM (YÊU THÍCH) ---
            override fun onFavoriteClick(song: Song, isFavorite: Boolean) {
                val db = FirebaseFirestore.getInstance()
                val songId = song.id ?: song.title

                if (isFavorite) {
                    // Thêm vào Favorites
                    db.collection("favorites").document(songId).set(song)
                        .addOnSuccessListener {
                            // Không cần Toast ở đây vì BottomSheet đã Toast rồi,
                            // hoặc log để debug
                            Log.d("Fav", "Đã lưu vào DB: ${song.title}")
                        }
                } else {
                    // Xóa khỏi Favorites
                    db.collection("favorites").document(songId).delete()
                        .addOnSuccessListener {
                            Log.d("Fav", "Đã xóa khỏi DB: ${song.title}")
                        }
                }
            }

            // --- CÁC NÚT CHỨC NĂNG KHÁC ---
            override fun onPlayNext(song: Song) {
                Toast.makeText(context, "Ưu tiên phát bài tiếp theo", Toast.LENGTH_SHORT).show()
            }

            override fun onAddToQueue(song: Song) {
                Toast.makeText(context, "Đã thêm vào hàng chờ", Toast.LENGTH_SHORT).show()
            }

            override fun onAddToPlaylist(song: Song) {
                Toast.makeText(context, "Tính năng đang phát triển", Toast.LENGTH_SHORT).show()
            }

            override fun onGoToAlbum(song: Song) {
                Toast.makeText(context, "Chuyển đến Album...", Toast.LENGTH_SHORT).show()
            }

            override fun onGoToArtist(song: Song) {
                Toast.makeText(context, "Nghệ sĩ: ${song.artist}", Toast.LENGTH_SHORT).show()
            }

            // --- XỬ LÝ XÓA BÀI HÁT (ADMIN) ---
            override fun onDeleteSong(song: Song) {
                // Xác nhận lần cuối trước khi xóa
                AlertDialog.Builder(requireContext())
                    .setTitle("Xóa vĩnh viễn?")
                    .setMessage("Bài hát '${song.title}' sẽ bị xóa khỏi cơ sở dữ liệu. Bạn có chắc không?")
                    .setPositiveButton("Xóa") { _, _ ->
                        deleteSongFromFirebase(song)
                    }
                    .setNegativeButton("Hủy", null)
                    .show()
            }
        })
        bottomSheet.show(parentFragmentManager, "SongOptionsBottomSheet")
    }

    // Hàm xóa bài hát thật sự khỏi Firebase
    private fun deleteSongFromFirebase(song: Song) {
        val db = FirebaseFirestore.getInstance()
        val songId = song.id ?: song.title

        db.collection("songs").document(songId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Đã xóa bài hát", Toast.LENGTH_SHORT).show()
                // Xóa khỏi list trên màn hình và cập nhật UI
                songList.remove(song)
                adapter.notifyDataSetChanged()
                tvTotalSongs?.text = "${songList.size} songs"
            }
            .addOnFailureListener {
                Toast.makeText(context, "Lỗi khi xóa: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}