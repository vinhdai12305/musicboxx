package com.finalexam.musicboxx.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity // Import MainActivity
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.ArtistsAdapter
import com.finalexam.musicboxx.model.Artist
import Song // Class model Song của bạn
import com.google.firebase.firestore.FirebaseFirestore

class HomeTabFragment : Fragment(R.layout.fragment_home_tab) {

    private lateinit var recentAdapter: SongsAdapter
    private lateinit var mostPlayedAdapter: SongsAdapter
    private lateinit var artistsAdapter: ArtistsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. XỬ LÝ NÚT SEARCH
        view.findViewById<ImageView>(R.id.ivSearch).setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

        // 2. Ánh xạ RecyclerView
        val rvRecentlyPlayed = view.findViewById<RecyclerView>(R.id.rvRecentlyPlayed)
        val rvMostPlayed = view.findViewById<RecyclerView>(R.id.rvMostPlayed)
        val rvArtists = view.findViewById<RecyclerView>(R.id.rvArtists)

        // ================================================================
        // 3. KHỞI TẠO ADAPTER VỚI LOGIC GIỐNG FAVORITES
        // ================================================================

        // Adapter cho Recently Played
        recentAdapter = SongsAdapter(emptyList()) { song ->
            // Gọi hàm playMusic bên MainActivity
            // Lưu ý: Kiểm tra biến 'audioUrl' hay 'fileUrl' trong class Song của bạn cho khớp
            (activity as? MainActivity)?.playMusic(song.audioUrl)
        }

        // Adapter cho Most Played
        mostPlayedAdapter = SongsAdapter(emptyList()) { song ->
            // Gọi hàm playMusic bên MainActivity
            (activity as? MainActivity)?.playMusic(song.audioUrl)
        }

        // Adapter cho Artist
        artistsAdapter = ArtistsAdapter(emptyList()) { artist ->
            // Xử lý khi click vào nghệ sĩ (nếu cần)
        }

        // 4. Setup RecyclerView (Ngang)
        setupHorizontalRecyclerView(rvRecentlyPlayed, recentAdapter)
        setupHorizontalRecyclerView(rvMostPlayed, mostPlayedAdapter)
        setupHorizontalRecyclerView(rvArtists, artistsAdapter)

        // 5. Tải dữ liệu
        fetchSongs()
        fetchArtists()
    }

    private fun setupHorizontalRecyclerView(rv: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv.setHasFixedSize(true)
    }

    private fun fetchSongs() {
        FirebaseFirestore.getInstance().collection("songs")
            .limit(10).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val list = documents.toObjects(Song::class.java)
                    recentAdapter.updateData(list)
                    mostPlayedAdapter.updateData(list.shuffled())
                }
            }
            .addOnFailureListener { e -> Log.e("FIREBASE", "Lỗi tải nhạc", e) }
    }

    private fun fetchArtists() {
        FirebaseFirestore.getInstance().collection("artist")
            .limit(10).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val list = documents.toObjects(Artist::class.java)
                    artistsAdapter.updateData(list)
                }
            }
            .addOnFailureListener { e -> Log.e("FIREBASE", "Lỗi tải nghệ sĩ", e) }
    }
}