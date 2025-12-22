package com.finalexam.musicboxx.hometab

import Song
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels // 1. Import ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongsListAdapter
import com.finalexam.musicboxx.home.HomeTabViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ArtistDetailsFragment : Fragment(R.layout.fragment_artist_details) {

    // 2. Kết nối ViewModel chung
    private val viewModel: HomeTabViewModel by activityViewModels()

    private var artistName: String = ""
    private var artistImage: String = ""

    private lateinit var adapter: SongsListAdapter
    private val artistSongs = mutableListOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Nhận dữ liệu String gửi từ màn hình trước
        arguments?.let {
            artistName = it.getString("artist_name", "")
            artistImage = it.getString("artist_image", "")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack: ImageView = view.findViewById(R.id.btnBack)
        val imgArtist: ImageView = view.findViewById(R.id.imgDetailArtist)
        val tvName: TextView = view.findViewById(R.id.tvDetailName)

        // ID này phải trùng với file fragment_artist_details.xml
        val rvSongs: RecyclerView = view.findViewById(R.id.rvArtistSongs)

        // UI
        tvName.text = if (artistName.isNotEmpty()) artistName else "Unknown Artist"

        Glide.with(this)
            .load(artistImage)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imgArtist)

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 3. Setup Adapter (Giống hệt Album)
        adapter = SongsListAdapter(
            artistSongs,
            onSongClick = { song ->
                // LOGIC PHÁT NHẠC
                val index = artistSongs.indexOf(song)
                if (index != -1) {
                    // Nạp danh sách bài của ca sĩ này vào ExoPlayer
                    viewModel.playUserList(artistSongs, index)
                }
            },
            onMoreClick = { song ->
                // Xử lý menu 3 chấm (tạm thời hiện Toast)
                Toast.makeText(context, "More: ${song.title}", Toast.LENGTH_SHORT).show()
            }
        )

        rvSongs.layoutManager = LinearLayoutManager(requireContext())
        rvSongs.adapter = adapter

        // 4. Tải bài hát từ Firebase
        loadSongsByArtist(artistName)
    }

    private fun loadSongsByArtist(name: String) {
        if (name.isEmpty()) return

        Log.d("ArtistDetail", "Đang tìm bài hát của ca sĩ: '$name'")

        // Tìm các bài hát có field 'artist' trùng khớp
        FirebaseFirestore.getInstance()
            .collection("songs")
            .whereEqualTo("artist", name.trim()) // trim() để xóa khoảng trắng thừa
            .get()
            .addOnSuccessListener { documents ->
                artistSongs.clear()
                for (document in documents) {
                    val song = document.toObject(Song::class.java)
                    song.id = document.id
                    artistSongs.add(song)
                }
                adapter.notifyDataSetChanged()

                if (artistSongs.isEmpty()) {
                    Toast.makeText(context, "Chưa có bài hát nào của $name", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Log.e("ArtistDetail", "Lỗi tải bài hát", it)
            }
    }
}