package com.finalexam.musicboxx.home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController // QUAN TRỌNG: Để chuyển trang
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.ArtistsAdapter
import com.finalexam.musicboxx.model.Artist // Kiểm tra lại package Artist của bạn
import Song // Kiểm tra lại import Song của bạn
import com.google.firebase.firestore.FirebaseFirestore

class HomeTabFragment : Fragment(R.layout.fragment_home_tab) {

    private lateinit var recentAdapter: SongsAdapter
    private lateinit var mostPlayedAdapter: SongsAdapter
    private lateinit var artistsAdapter: ArtistsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- 1. XỬ LÝ NÚT SEARCH (MỚI THÊM) ---
        val ivSearch = view.findViewById<ImageView>(R.id.ivSearch)
        ivSearch.setOnClickListener {
            // Chuyển sang màn hình SearchFragment
            findNavController().navigate(R.id.searchFragment)
        }
        // --------------------------------------

        // 2. Ánh xạ RecyclerView
        val rvRecentlyPlayed = view.findViewById<RecyclerView>(R.id.rvRecentlyPlayed)
        val rvArtists = view.findViewById<RecyclerView>(R.id.rvArtists)
        val rvMostPlayed = view.findViewById<RecyclerView>(R.id.rvMostPlayed)

        // 3. Khởi tạo Adapter
        recentAdapter = SongsAdapter(emptyList()) { song -> /* Play music logic */ }
        mostPlayedAdapter = SongsAdapter(emptyList()) { song -> /* Play music logic */ }
        artistsAdapter = ArtistsAdapter(emptyList()) { artist -> /* Artist click logic */ }

        // 4. Setup RecyclerView
        setupHorizontalRecyclerView(rvRecentlyPlayed, recentAdapter)
        setupHorizontalRecyclerView(rvArtists, artistsAdapter)
        setupHorizontalRecyclerView(rvMostPlayed, mostPlayedAdapter)

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
                    recentAdapter.updateData(list) // Đảm bảo adapter có hàm updateData
                    mostPlayedAdapter.updateData(list.shuffled())
                }
            }
            .addOnFailureListener { e -> Log.e("FIREBASE", "Lỗi tải nhạc", e) }
    }

    private fun fetchArtists() {
        // Lưu ý: Kiểm tra tên collection là "artists" hay "artist" trên Firebase của bạn
        FirebaseFirestore.getInstance().collection("artists")
            .limit(10).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val list = documents.toObjects(Artist::class.java)
                    artistsAdapter.updateData(list) // Đảm bảo adapter có hàm updateData
                }
            }
            .addOnFailureListener { e -> Log.e("FIREBASE", "Lỗi tải nghệ sĩ", e) }
    }
}