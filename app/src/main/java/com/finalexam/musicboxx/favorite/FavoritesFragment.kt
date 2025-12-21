package com.finalexam.musicboxx.favorite

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController // QUAN TRỌNG
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.FavoritesAdapter
import Song // Kiểm tra lại import Song
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- 1. XỬ LÝ NÚT SEARCH (MỚI THÊM) ---
        view.findViewById<View>(R.id.ivSearch)?.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }
        // --------------------------------------

        val rvFavorites = view.findViewById<RecyclerView>(R.id.rvFavorites)
        val tvCount = view.findViewById<TextView>(R.id.tvCount)

        // 2. Setup Adapter
        favoritesAdapter = FavoritesAdapter(ArrayList()) { song ->
            // Logic phát nhạc
            Toast.makeText(context, "Play: ${song.title}", Toast.LENGTH_SHORT).show()
            (activity as? MainActivity)?.playMusic(song.audioUrl)
        }

        rvFavorites.adapter = favoritesAdapter
        rvFavorites.layoutManager = LinearLayoutManager(context)

        // 3. Tải dữ liệu
        fetchFavoriteSongs()
    }

    private fun fetchFavoriteSongs() {
        val db = FirebaseFirestore.getInstance()
        // Lấy dữ liệu demo (sau này sửa query để lấy đúng bài yêu thích của User)
        db.collection("songs").limit(20).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val list = documents.toObjects(Song::class.java)
                    favoritesAdapter.updateData(list) // Đảm bảo adapter có hàm updateData

                    // Cập nhật text số lượng
                    view?.findViewById<TextView>(R.id.tvCount)?.text = "${list.size} favorites"
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
    }
}