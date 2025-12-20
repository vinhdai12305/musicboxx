package com.finalexam.musicboxx.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import Song // Import Model Song (Cái này của bạn đang đúng nếu file Song nằm ngay thư mục gốc)

// --- SỬA LẠI ĐOẠN IMPORT NÀY CHO ĐÚNG ĐƯỜNG DẪN ---
import com.finalexam.musicboxx.adapter.ArtistsAdapter
import com.finalexam.musicboxx.model.Artist

class HomeTabFragment : Fragment(R.layout.fragment_home_tab) {

    private lateinit var recentAdapter: SongsAdapter
    private lateinit var mostPlayedAdapter: SongsAdapter
    private lateinit var artistsAdapter: ArtistsAdapter

    private var songList: ArrayList<Song> = ArrayList()
    private var artistList: ArrayList<Artist> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ánh xạ
        val rvRecentlyPlayed = view.findViewById<RecyclerView>(R.id.rvRecentlyPlayed)
        val rvArtists = view.findViewById<RecyclerView>(R.id.rvArtists)
        val rvMostPlayed = view.findViewById<RecyclerView>(R.id.rvMostPlayed)

        // Click Song
        val onSongClick: (Song) -> Unit = { song ->
            Toast.makeText(context, "Phát: ${song.title}", Toast.LENGTH_SHORT).show()
            (activity as? MainActivity)?.playMusic(song.audioUrl)
        }

        // Click Artist
        val onArtistClick: (Artist) -> Unit = { artist ->
            Toast.makeText(context, "Ca sĩ: ${artist.name}", Toast.LENGTH_SHORT).show()
        }

        // Khởi tạo Adapter
        recentAdapter = SongsAdapter(songList, onSongClick)
        mostPlayedAdapter = SongsAdapter(songList, onSongClick)
        artistsAdapter = ArtistsAdapter(artistList, onArtistClick)

        // Setup RecyclerView
        setupHorizontalRecyclerView(rvRecentlyPlayed, recentAdapter)
        setupHorizontalRecyclerView(rvArtists, artistsAdapter)
        setupHorizontalRecyclerView(rvMostPlayed, mostPlayedAdapter)

        // Load data
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