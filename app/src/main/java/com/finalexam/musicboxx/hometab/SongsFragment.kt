package com.finalexam.musicboxx.hometab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongsListAdapter
import Song
import com.google.firebase.firestore.FirebaseFirestore

// IMPORT MỚI: Để dùng BottomSheet và Interface
import com.finalexam.musicboxx.bottomsheet.SongOptionsBottomSheet
import com.finalexam.musicboxx.bottomsheet.SongOptionListener

class SongsFragment : Fragment(R.layout.fragment_songs) {

    private lateinit var adapter: SongsListAdapter
    private val songList = mutableListOf<Song>()
    private var tvTotalSongs: TextView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvSongs = view.findViewById<RecyclerView>(R.id.rvSongs)
        tvTotalSongs = view.findViewById(R.id.tvTotalSongs)

        // 2. Setup Adapter
        // CẬP NHẬT: Thêm onMoreClick, logic onSongClick giữ nguyên 100%
        adapter = SongsListAdapter(songList,
            onSongClick = { song ->
                // --- (NỘI DUNG CŨ GIỮ NGUYÊN) ---
                if (song.audioUrl.isNotEmpty()) {
                    val mainActivity = activity as? MainActivity
                    if (mainActivity != null) {
                        mainActivity.playMusic(song.audioUrl)
                    } else {
                        Log.e("SongsFragment", "Lỗi: Activity không phải MainActivity")
                    }
                } else {
                    Toast.makeText(context, "Bài hát này chưa có link nhạc", Toast.LENGTH_SHORT).show()
                }
            },
            onMoreClick = { song ->
                // --- (MỚI) XỬ LÝ KHI BẤM 3 CHẤM ---
                showBottomSheet(song)
            }
        )

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

    // --- (MỚI) HÀM HIỂN THỊ BOTTOM SHEET VÀ XỬ LÝ SỰ KIỆN ---
    private fun showBottomSheet(song: Song) {
        val bottomSheet = SongOptionsBottomSheet(song, object : SongOptionListener {

            override fun onPlayNext(song: Song) {
                Toast.makeText(context, "Play Next: ${song.title}", Toast.LENGTH_SHORT).show()
            }

            override fun onAddToQueue(song: Song) {
                Toast.makeText(context, "Added to Queue: ${song.title}", Toast.LENGTH_SHORT).show()
            }

            override fun onAddToPlaylist(song: Song) {
                Toast.makeText(context, "Add to Playlist clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onGoToAlbum(song: Song) {
                Toast.makeText(context, "Go to Album clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onGoToArtist(song: Song) {
                Toast.makeText(context, "Go to Artist: ${song.artist}", Toast.LENGTH_SHORT).show()
            }

            override fun onDeleteSong(song: Song) {
                // Ví dụ logic xóa (cần code backend thực tế để xóa trên Firebase)
                Toast.makeText(context, "Đã gửi yêu cầu xóa: ${song.title}", Toast.LENGTH_SHORT).show()
            }
        })

        bottomSheet.show(parentFragmentManager, "SongOptionsBottomSheet")
    }
}