package com.finalexam.musicboxx.hometab

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongsListAdapter
import Song
import com.google.firebase.firestore.FirebaseFirestore

class SongsFragment : Fragment(R.layout.fragment_songs) { // Nhớ trỏ đúng layout xml

    private lateinit var adapter: SongsListAdapter
    private val songList = mutableListOf<Song>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvSongs = view.findViewById<RecyclerView>(R.id.rvSongs)

        // Setup Adapter
        adapter = SongsListAdapter(songList) { song ->
            // Xử lý khi bấm vào bài hát (VD: Mở Player)
            Log.d("SongsFragment", "Clicked: ${song.title}")
        }

        rvSongs.layoutManager = LinearLayoutManager(context)
        rvSongs.adapter = adapter

        // Lấy dữ liệu từ Firebase
        fetchSongsFromFirebase()
    }

    private fun fetchSongsFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("songs") // Tên collection trên Firebase
            .get()
            .addOnSuccessListener { documents ->
                songList.clear()
                for (document in documents) {
                    val song = document.toObject(Song::class.java)
                    songList.add(song)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("SongsFragment", "Error getting documents: ", exception)
            }
    }
}