package com.finalexam.musicboxx.hometab

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation // Thêm import này để dùng Navigation.findNavController
import androidx.navigation.fragment.findNavController
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

        // ---------- RecyclerView ----------
        rvArtists = view.findViewById(R.id.rvAllArtists)
        rvArtists.layoutManager = LinearLayoutManager(requireContext())
        rvArtists.setHasFixedSize(true)

        // ---------- Other views ----------
        progressBar = view.findViewById(R.id.progressBar)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        tvArtistCount = view.findViewById(R.id.tvArtistCount)
        tvSort = view.findViewById(R.id.tvSort)

        // Sort (tạm thời)
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
                // Kiểm tra xem Fragment còn sống không trước khi update UI
                if (!isAdded || context == null) return@addOnSuccessListener

                progressBar.visibility = View.GONE

                if (!documents.isEmpty) {
                    val artistList = documents.toObjects(Artist::class.java)

                    tvArtistCount.text = "${artistList.size} artists"
                    tvEmpty.visibility = View.GONE

                    adapter = ArtistsHomeTabAdapter(
                        artistsList = artistList,

                        // ================= CLICK ITEM → NAVIGATION =================
                        onClick = { artist ->
                            val bundle = Bundle().apply {
                                putSerializable("artist", artist)
                            }

                            try {
                                // ------------------------------------------------------------------
                                // 🔥 CÁCH KHẮC PHỤC TRIỆT ĐỂ CHO VIEW PAGER / TABS 🔥
                                // ------------------------------------------------------------------

                                // Cách 1: Thử dùng cách chuẩn
                                // findNavController().navigate(R.id.action_global_artistDetailsFragment, bundle)

                                // Cách 2 (MẠNH HƠN): Tìm NavController từ Activity cha
                                // LƯU Ý: 'R.id.nav_host_fragment' là ID phổ biến trong activity_main.xml
                                // Nếu code báo đỏ chữ nav_host_fragment, hãy mở activity_main.xml xem ID là gì rồi sửa lại ở đây.
                                val mainNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                                mainNavController.navigate(R.id.action_global_artistDetailsFragment, bundle)

                            } catch (e: Exception) {
                                Log.e("NavError", "Cách 2 lỗi, thử fallback về cách 1: ${e.message}")
                                // Fallback: Nếu cách tìm Activity lỗi (do sai ID), thử lại cách thường
                                try {
                                    findNavController().navigate(R.id.action_global_artistDetailsFragment, bundle)
                                } catch (e2: Exception) {
                                    Log.e("NavError", "Thất bại toàn tập: ${e2.message}")
                                    Toast.makeText(requireContext(), "Lỗi điều hướng: ${e2.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        },

                        // ================= CLICK 3 CHẤM → BOTTOM SHEET =================
                        onMoreClick = { artist ->
                            val bottomSheet = ArtistOptionsBottomSheet(artist)
                            bottomSheet.show(parentFragmentManager, "ArtistOptionsBottomSheet")
                        }
                    )

                    rvArtists.adapter = adapter
                } else {
                    tvArtistCount.text = "0 artists"
                    tvEmpty.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { e ->
                if (!isAdded) return@addOnFailureListener
                progressBar.visibility = View.GONE
                tvArtistCount.text = "0 artists"
                tvEmpty.visibility = View.VISIBLE
                Log.e("ArtistsFragmentHomeTab", "Error loading artists", e)
            }
    }
}