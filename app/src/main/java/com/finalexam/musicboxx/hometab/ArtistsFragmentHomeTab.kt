package com.finalexam.musicboxx.hometab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager // Import cái này
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.ArtistsHomeTabAdapter
import com.finalexam.musicboxx.model.Artist
import com.google.firebase.firestore.FirebaseFirestore

class ArtistsFragmentHomeTab : Fragment(R.layout.fragment_artists_fragment_home_tab) {

    private lateinit var rvArtists: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var adapter: ArtistsHomeTabAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ánh xạ View
        rvArtists = view.findViewById(R.id.rvAllArtists)
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        // 2. Cấu hình RecyclerView
        // SỬA Ở ĐÂY: Dùng LinearLayoutManager để hiển thị 1 hàng dọc (List)
        rvArtists.layoutManager = LinearLayoutManager(context)
        rvArtists.setHasFixedSize(true)

        // 3. Tải dữ liệu
        fetchArtists()
    }

    private fun fetchArtists() {
        progressBar.visibility = View.VISIBLE

        FirebaseFirestore.getInstance().collection("artist")
            .get()
            .addOnSuccessListener { documents ->
                progressBar.visibility = View.GONE

                if (!documents.isEmpty) {
                    val artistList = documents.toObjects(Artist::class.java)

                    adapter = ArtistsHomeTabAdapter(artistList) { artist ->
                        Toast.makeText(context, "Clicked: ${artist.name}", Toast.LENGTH_SHORT).show()
                    }

                    rvArtists.adapter = adapter
                    tvEmpty.visibility = View.GONE
                } else {
                    tvEmpty.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                Log.e("ArtistsTab", "Error loading artists", e)
            }
    }
}