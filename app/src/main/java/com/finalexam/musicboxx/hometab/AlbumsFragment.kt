package com.finalexam.musicboxx.hometab

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.AlbumsAdapter
import com.finalexam.musicboxx.model.Album
import com.google.firebase.firestore.FirebaseFirestore

class AlbumsFragment : Fragment(R.layout.fragment_albums) {

    private lateinit var adapter: AlbumsAdapter
    private val albumList = mutableListOf<Album>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvAlbums = view.findViewById<RecyclerView>(R.id.rvAlbums)

        adapter = AlbumsAdapter(albumList)

        // Grid 2 cột
        rvAlbums.layoutManager = GridLayoutManager(context, 2)
        rvAlbums.adapter = adapter

        fetchAlbumsFromFirebase()
    }

    private fun fetchAlbumsFromFirebase() {
        val db = FirebaseFirestore.getInstance()

        db.collection("album")
            .get()
            .addOnSuccessListener { documents ->
                albumList.clear()
                for (document in documents) {
                    val album = document.toObject(Album::class.java)
                    albumList.add(album)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("AlbumsFragment", "Error getting documents: ", exception)
            }
    }
}