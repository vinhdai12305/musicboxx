package com.finalexam.musicboxx.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.MusicSquareAdapter
import com.finalexam.musicboxx.adapter.ArtistCircleAdapter
import com.finalexam.musicboxx.data.repository.SongRepository
import com.finalexam.musicboxx.data.repository.ArtistRepository
import com.finalexam.musicboxx.model.MusicItem
import com.finalexam.musicboxx.model.Song
import com.finalexam.musicboxx.model.ArtistItem
import com.finalexam.musicboxx.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SuggestedFragment : Fragment() {

    // ===============================
    // REPOSITORIES (SOURCE OF TRUTH)
    // ===============================
    private val songRepository by lazy {
        SongRepository(FirebaseFirestore.getInstance())
    }

    private val artistRepository by lazy {
        ArtistRepository(FirebaseFirestore.getInstance())
    }

    // ===============================
    // ADAPTERS
    // ===============================
    private lateinit var recentAdapter: MusicSquareAdapter
    private lateinit var mostPlayedAdapter: MusicSquareAdapter
    private lateinit var artistAdapter: ArtistCircleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_suggested, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews(view)
        loadHomeData()
    }

    // ===============================
    // SETUP RECYCLERVIEWS (NO DATA)
    // ===============================
    private fun setupRecyclerViews(view: View) {

        // Recently Played
        recentAdapter = MusicSquareAdapter(emptyList())
        view.findViewById<RecyclerView>(R.id.recycler_recently_played).apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recentAdapter
        }

        // Most Played
        mostPlayedAdapter = MusicSquareAdapter(emptyList())
        view.findViewById<RecyclerView>(R.id.recycler_most_played).apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mostPlayedAdapter
        }

        // Artists
        artistAdapter = ArtistCircleAdapter(emptyList())
        view.findViewById<RecyclerView>(R.id.recycler_artists).apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = artistAdapter
        }
    }

    // ===============================
    // LOAD DATA FROM REPOSITORIES
    // ===============================
    private fun loadHomeData() {

        // Recently Played (tạm dùng all songs)
        lifecycleScope.launch {
            when (val result = songRepository.getAllSongs()) {
                is Resource.Success -> {
                    recentAdapter.updateData(
                        mapSongsToMusicItems(result.data ?: emptyList())
                    )
                }
                is Resource.Error -> {
                    // TODO: show error nếu cần
                }
                else -> {}
            }
        }

        // Most Played (Trending)
        lifecycleScope.launch {
            when (val result = songRepository.getTrendingSongs()) {
                is Resource.Success -> {
                    mostPlayedAdapter.updateData(
                        mapSongsToMusicItems(result.data ?: emptyList())
                    )
                }
                is Resource.Error -> {
                    // TODO: show error nếu cần
                }
                else -> {}
            }
        }

        // Artists
        lifecycleScope.launch {
            when (val result = artistRepository.getAllArtists()) {
                is Resource.Success -> {
                    artistAdapter.updateData(result.data ?: emptyList())
                }
                is Resource.Error -> {
                    // TODO: show error nếu cần
                }
                else -> {}
            }
        }
    }

    // ===============================
    // MAP DOMAIN → UI MODEL
    // ===============================
    private fun mapSongsToMusicItems(songs: List<Song>): List<MusicItem> {
        return songs.map { song ->
            MusicItem(
                id = song.id ?: "",
                title = song.title ?: "",
                artist = song.artist ?: "",
                imageResource = R.drawable.ic_launcher_foreground
            )
        }
    }
}
