package com.finalexam.musicboxx.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.ArtistsAdapter
import com.finalexam.musicboxx.home.SongsAdapter
import com.finalexam.musicboxx.model.Artist
import Song
import androidx.navigation.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.finalexam.musicboxx.hometab.ArtistDetailsFragment

class SuggestedFragment : Fragment(R.layout.fragment_suggested) {

    private lateinit var recentAdapter: SongsAdapter
    private lateinit var mostPlayedAdapter: SongsAdapter
    private lateinit var artistsAdapter: ArtistsAdapter

    // 1. Dùng activityViewModels để dùng chung ViewModel với Home/Favorites
    private val viewModel: HomeTabViewModel by activityViewModels()

    private var listRecent = listOf<Song>()
    private var listMostPlayed = listOf<Song>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvRecentlyPlayed = view.findViewById<RecyclerView>(R.id.rvRecentlyPlayed)
        val rvMostPlayed = view.findViewById<RecyclerView>(R.id.rvMostPlayed)
        val rvArtists = view.findViewById<RecyclerView>(R.id.rvArtists)

        // --- 2. SETUP ADAPTER (SỬA LỖI TẠI ĐÂY) ---

        // ADAPTER RECENT
        recentAdapter = SongsAdapter(emptyList(),
            onSongClick = { clickedSong ->
                val index = listRecent.indexOf(clickedSong)
                if (index != -1) viewModel.playUserList(listRecent, index)
            },
            onMoreClick = { song ->
                Toast.makeText(context, "More: ${song.title}", Toast.LENGTH_SHORT).show()
            }
        )

        // ADAPTER MOST PLAYED
        mostPlayedAdapter = SongsAdapter(emptyList(),
            onSongClick = { clickedSong ->
                val index = listMostPlayed.indexOf(clickedSong)
                if (index != -1) viewModel.playUserList(listMostPlayed, index)
            },
            onMoreClick = { song ->
                Toast.makeText(context, "More: ${song.title}", Toast.LENGTH_SHORT).show()
            }
        )

        // ADAPTER ARTIST - [ĐÃ BỔ SUNG LOGIC CHUYỂN MÀN HÌNH]
        artistsAdapter = ArtistsAdapter(emptyList()) { artist ->
            // 1. Khởi tạo Fragment chi tiết
            val detailFragment = ArtistDetailsFragment()

            // 2. Truyền dữ liệu Artist sang (Artist cần implements Serializable)
            val bundle = Bundle()
            bundle.putSerializable("artist", artist)
            detailFragment.arguments = bundle

            // 3. Thực hiện chuyển Fragment
            // --- Trong file SuggestedFragment.kt ---

            requireActivity()
                .findNavController(R.id.nav_host_fragment)
                .navigate(R.id.artistDetailsFragment, bundle)
        }

        // 3. Gắn Adapter
        if (rvRecentlyPlayed != null) setupHorizontalRecyclerView(rvRecentlyPlayed, recentAdapter)
        if (rvMostPlayed != null) setupHorizontalRecyclerView(rvMostPlayed, mostPlayedAdapter)
        if (rvArtists != null) setupHorizontalRecyclerView(rvArtists, artistsAdapter)

        // 4. Tải dữ liệu
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
            .limit(20).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val fullList = documents.toObjects(Song::class.java)

                    listRecent = fullList
                    listMostPlayed = fullList.shuffled()

                    // Gọi hàm updateData
                    recentAdapter.updateData(listRecent)
                    mostPlayedAdapter.updateData(listMostPlayed)
                }
            }
    }

    private fun fetchArtists() {
        FirebaseFirestore.getInstance().collection("artist")
            .limit(10).get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val list = documents.toObjects(Artist::class.java)
                    artistsAdapter.updateData(list)
                }
            }
    }
}