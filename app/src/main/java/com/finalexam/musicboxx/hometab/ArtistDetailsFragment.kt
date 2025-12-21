package com.finalexam.musicboxx.hometab

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.google.firebase.firestore.FirebaseFirestore

class ArtistDetailsFragment : Fragment(R.layout.fragment_artist_details) {

    private var artistName: String = ""
    private var artistImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ KEY PHẢI TRÙNG VỚI NƠI GỬI
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
        val rvSongs: RecyclerView = view.findViewById(R.id.rvArtistSongs)

        // ---------- UI ----------
        tvName.text = if (artistName.isNotEmpty()) artistName else "Unknown Artist"

        Glide.with(this)
            .load(artistImage)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imgArtist)

        // ---------- BACK (NAVIGATION COMPONENT) ----------
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // ---------- RecyclerView ----------
        rvSongs.layoutManager = LinearLayoutManager(requireContext())

        // ---------- LOAD SONGS TỪ FIREBASE ----------
        loadSongsByArtist(artistName)
    }

    // ================= LOAD SONGS =================
    private fun loadSongsByArtist(artistName: String) {
        if (artistName.isEmpty()) return

        FirebaseFirestore.getInstance()
            .collection("songs")
            .whereEqualTo("artist", artistName)
            .get()
            .addOnSuccessListener { documents ->
                // TODO:
                // val songs = documents.map { ... }
                // rvSongs.adapter = SongsAdapter(songs)
            }
    }
}
