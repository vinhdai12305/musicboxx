package com.finalexam.musicboxx.hometab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.ArtistsHomeTabAdapter
import com.finalexam.musicboxx.model.Artist
import com.google.firebase.firestore.FirebaseFirestore

class ArtistsFragmentHomeTab : Fragment(R.layout.fragment_artists_fragment_home_tab) {

    private lateinit var rvArtists: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvEmpty: TextView
    private lateinit var tvArtistCount: TextView
    private lateinit var tvSort: TextView
    private lateinit var adapter: ArtistsHomeTabAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView
        rvArtists = view.findViewById(R.id.rvAllArtists)
        rvArtists.layoutManager = LinearLayoutManager(requireContext())
        rvArtists.setHasFixedSize(true)

        // Other views
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        // Header views (BỔ SUNG)
        tvArtistCount = view.findViewById(R.id.tvArtistCount)
        tvSort = view.findViewById(R.id.tvSort)

        // (Tuỳ chọn) click sort
        tvSort.setOnClickListener {
            Toast.makeText(requireContext(), "Sort by Date Added", Toast.LENGTH_SHORT).show()
        }

        fetchArtists()
    }

    private fun fetchArtists() {
        progressBar.visibility = View.VISIBLE

        FirebaseFirestore.getInstance()
            .collection("artist")
            .get()
            .addOnSuccessListener { documents ->
                progressBar.visibility = View.GONE

                if (!documents.isEmpty) {
                    val artistList = documents.toObjects(Artist::class.java)

                    // ✅ HIỂN THỊ TỔNG SỐ NGHỆ SĨ
                    tvArtistCount.text = "${artistList.size} artists"

                    adapter = ArtistsHomeTabAdapter(artistList) { artist ->
                        Toast.makeText(
                            requireContext(),
                            "Clicked: ${artist.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    rvArtists.adapter = adapter
                    tvEmpty.visibility = View.GONE
                } else {
                    tvArtistCount.text = "0 artists"
                    tvEmpty.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { e ->
                progressBar.visibility = View.GONE
                Log.e("ArtistsTab", "Error loading artists", e)
            }
    }
}
