package com.finalexam.musicboxx.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.ArtistsAdapter
import com.finalexam.musicboxx.model.Artist
import Song // Import đúng file Song của bạn
import com.google.firebase.firestore.FirebaseFirestore

class SuggestedFragment : Fragment(R.layout.fragment_suggested) {

    private lateinit var recentAdapter: SongsAdapter
    private lateinit var mostPlayedAdapter: SongsAdapter
    private lateinit var artistsAdapter: ArtistsAdapter
    private lateinit var viewModel: HomeTabViewModel // Khai báo ViewModel

    // Danh sách tạm để giữ dữ liệu
    private var listRecent = listOf<Song>()
    private var listMostPlayed = listOf<Song>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Lấy ViewModel chung
        viewModel = ViewModelProvider(requireActivity())[HomeTabViewModel::class.java]

        val rvRecentlyPlayed = view.findViewById<RecyclerView>(R.id.rvRecentlyPlayed)
        val rvMostPlayed = view.findViewById<RecyclerView>(R.id.rvMostPlayed)
        val rvArtists = view.findViewById<RecyclerView>(R.id.rvArtists)

        // 2. Setup Adapter với sự kiện Click

        // --- ADAPTER 1: RECENT ---
        recentAdapter = SongsAdapter(emptyList()) { clickedSong ->
            // Khi bấm vào bài hát:
            // A. Phát nhạc (truyền bài hát + cả danh sách để next/prev)
            viewModel.playTrack(clickedSong, listRecent)

        }

        // --- ADAPTER 2: MOST PLAYED ---
        mostPlayedAdapter = SongsAdapter(emptyList()) { clickedSong ->
            viewModel.playTrack(clickedSong, listMostPlayed)

            val playerDialog = NowPlayingFragment()
            playerDialog.show(parentFragmentManager, "NowPlaying")
        }

        // --- ADAPTER 3: ARTIST ---
        artistsAdapter = ArtistsAdapter(emptyList()) { }

        // 3. Gắn Adapter
        if (rvRecentlyPlayed != null) setupHorizontalRecyclerView(rvRecentlyPlayed, recentAdapter)
        if (rvMostPlayed != null) setupHorizontalRecyclerView(rvMostPlayed, mostPlayedAdapter)
        if (rvArtists != null) setupHorizontalRecyclerView(rvArtists, artistsAdapter)

        // 4. Tải dữ liệu
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
            .limit(20).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val fullList = documents.toObjects(Song::class.java)

                    // Cập nhật list local để dùng khi click
                    listRecent = fullList
                    listMostPlayed = fullList.shuffled()

                    // Cập nhật lên giao diện
                    recentAdapter.updateData(listRecent)
                    mostPlayedAdapter.updateData(listMostPlayed)
                }
            }
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