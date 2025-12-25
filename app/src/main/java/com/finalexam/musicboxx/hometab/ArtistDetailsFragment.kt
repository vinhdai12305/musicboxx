package com.finalexam.musicboxx.hometab

import Song
import android.os.Build // Bổ sung để check version Android
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongsListAdapter
import com.finalexam.musicboxx.home.HomeTabViewModel
import com.finalexam.musicboxx.model.Artist // Bổ sung import Artist
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable // Bổ sung

class ArtistDetailsFragment : Fragment(R.layout.fragment_artist_details) {

    // Kết nối ViewModel chung
    private val viewModel: HomeTabViewModel by activityViewModels()

    private var artistName: String = ""
    private var artistImage: String = ""

    private lateinit var adapter: SongsListAdapter
    private val artistSongs = mutableListOf<Song>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- PHẦN BỔ SUNG SỬA LỖI NHẬN DỮ LIỆU ---
        arguments?.let { bundle ->
            // Vì bên SuggestedFragment gửi sang là Object "artist", nên ta phải nhận là Serializable
            val artist = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getSerializable("artist", Artist::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getSerializable("artist") as? Artist
            }

            // Nếu nhận được object, ta tách lấy tên và ảnh gán vào biến cũ của bạn
            // để logic bên dưới không phải sửa gì cả.
            if (artist != null) {
                artistName = artist.name
                artistImage = artist.image
            }
        }
        // ------------------------------------------
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack: ImageView = view.findViewById(R.id.btnBack)
        val imgArtist: ImageView = view.findViewById(R.id.imgDetailArtist)
        val tvName: TextView = view.findViewById(R.id.tvDetailName)
        val rvSongs: RecyclerView = view.findViewById(R.id.rvArtistSongs)

        // UI (Giữ nguyên logic cũ)
        tvName.text = if (artistName.isNotEmpty()) artistName else "Unknown Artist"

        Glide.with(this)
            .load(artistImage)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imgArtist)

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // Setup Adapter (Giữ nguyên logic cũ)
        adapter = SongsListAdapter(
            artistSongs,
            onSongClick = { song ->
                val index = artistSongs.indexOf(song)
                if (index != -1) {
                    viewModel.playUserList(artistSongs, index)
                }
            },
            onMoreClick = { song ->
                Toast.makeText(context, "More: ${song.title}", Toast.LENGTH_SHORT).show()
            }
        )

        rvSongs.layoutManager = LinearLayoutManager(requireContext())
        rvSongs.adapter = adapter

        // Tải bài hát từ Firebase (Giữ nguyên logic cũ)
        loadSongsByArtist(artistName)
    }

    private fun loadSongsByArtist(name: String) {
        if (name.isEmpty()) return

        Log.d("ArtistDetail", "Đang tìm bài hát của ca sĩ: '$name'")

        FirebaseFirestore.getInstance()
            .collection("songs")
            .whereEqualTo("artist", name.trim())
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