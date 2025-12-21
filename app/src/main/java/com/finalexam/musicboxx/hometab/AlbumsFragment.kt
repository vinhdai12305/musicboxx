package com.finalexam.musicboxx.hometab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.AlbumsAdapter
import com.finalexam.musicboxx.model.Album
import com.google.firebase.firestore.FirebaseFirestore

class AlbumsFragment : Fragment(R.layout.fragment_albums) {

    private lateinit var adapter: AlbumsAdapter
    private val albumList = mutableListOf<Album>()
    private lateinit var tvTotalAlbums: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAlbums = view.findViewById<RecyclerView>(R.id.rvAlbums)
        tvTotalAlbums = view.findViewById(R.id.tvTotalAlbums)

        // Adapter + click album
        adapter = AlbumsAdapter(albumList) { clickedAlbum ->
            openAlbumDetail(clickedAlbum)
        }

        rvAlbums.layoutManager = GridLayoutManager(requireContext(), 2)
        rvAlbums.adapter = adapter

        fetchAlbumsFromFirebase()
    }

    // ================= OPEN ALBUM DETAIL (NAVIGATION) =================
    private fun openAlbumDetail(album: Album) {
        val bundle = Bundle().apply {
            putSerializable("album_data", album)
        }

        requireActivity()
            .findNavController(R.id.nav_host_fragment)
            .navigate(R.id.albumDetailFragment, bundle)
    }

    // ================= FETCH ALBUMS =================
    private fun fetchAlbumsFromFirebase() {
        FirebaseFirestore.getInstance()
            .collection("album")
            .get()
            .addOnSuccessListener { documents ->
                albumList.clear()

                documents.forEach { document ->
                    val album = document.toObject(Album::class.java)
                    albumList.add(album)
                }

                tvTotalAlbums.text = "${albumList.size} albums"
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("AlbumsFragment", "Error getting albums", exception)
                tvTotalAlbums.text = "0 albums"
            }
    }
}
