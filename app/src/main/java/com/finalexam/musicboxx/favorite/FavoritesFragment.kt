package com.finalexam.musicboxx.favorite

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.FavoritesAdapter
import Song
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList


class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvFavorites = view.findViewById<RecyclerView>(R.id.rvFavorites)
        val tvCount = view.findViewById<TextView>(R.id.tvCount)

        // 1. Setup Adapter
        favoritesAdapter = FavoritesAdapter(ArrayList()) { song ->
            // Xử lý khi click vào bài hát (phát nhạc)
            Toast.makeText(context, "Play: ${song.title}", Toast.LENGTH_SHORT).show()
            (activity as? MainActivity)?.playMusic(song.audioUrl)
        }

        rvFavorites.adapter = favoritesAdapter
        rvFavorites.layoutManager = LinearLayoutManager(context) // Xếp dọc

        // 2. Lấy dữ liệu (Ví dụ lấy từ Firebase hoặc Fake data để test)
        fetchFavoriteSongs()
    }

    private fun fetchFavoriteSongs() {
        val db = FirebaseFirestore.getInstance()
        // Giả sử bạn lấy tất cả bài hát (sau này sẽ lọc bài yêu thích sau)
        db.collection("songs").limit(20).get()
            .addOnSuccessListener { documents ->
                val list = documents.toObjects(Song::class.java)
                favoritesAdapter.updateData(list)

                // Cập nhật số lượng bài hát
                view?.findViewById<TextView>(R.id.tvCount)?.text = "${list.size} favorites"
            }
    }
}
