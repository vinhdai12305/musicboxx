package com.finalexam.musicboxx.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.ArtistsAdapter
import com.finalexam.musicboxx.model.Artist
import Song // Import đúng model Song của bạn
import com.google.firebase.firestore.FirebaseFirestore

// QUAN TRỌNG: Phải dùng layout fragment_suggested (nơi chứa các RecyclerView)
class SuggestedFragment : Fragment(R.layout.fragment_suggested) {

    private lateinit var recentAdapter: SongsAdapter
    private lateinit var mostPlayedAdapter: SongsAdapter
    private lateinit var artistsAdapter: ArtistsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ánh xạ (Bây giờ mới tìm thấy view vì đang ở đúng layout)
        val rvRecentlyPlayed = view.findViewById<RecyclerView>(R.id.rvRecentlyPlayed)
        val rvMostPlayed = view.findViewById<RecyclerView>(R.id.rvMostPlayed)
        val rvArtists = view.findViewById<RecyclerView>(R.id.rvArtists) // ID này cần có trong fragment_suggested.xml

        // Kiểm tra null để tránh crash nếu lỡ quên thêm ID vào XML
        if (rvRecentlyPlayed == null || rvMostPlayed == null) {
            Log.e("SuggestedFragment", "Không tìm thấy RecyclerView trong XML")
            return
        }

        // 2. Setup Adapter
        recentAdapter = SongsAdapter(emptyList()) { song ->
            (activity as? MainActivity)?.playMusic(song.audioUrl)
        }
        mostPlayedAdapter = SongsAdapter(emptyList()) { song ->
            (activity as? MainActivity)?.playMusic(song.audioUrl)
        }
        artistsAdapter = ArtistsAdapter(emptyList()) { artist ->
            // Xử lý click artist
        }

        setupHorizontalRecyclerView(rvRecentlyPlayed, recentAdapter)
        setupHorizontalRecyclerView(rvMostPlayed, mostPlayedAdapter)

        // Nếu có rvArtists thì setup
        if (rvArtists != null) {
            setupHorizontalRecyclerView(rvArtists, artistsAdapter)
        }

        // 3. Fetch Data
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
    }
}