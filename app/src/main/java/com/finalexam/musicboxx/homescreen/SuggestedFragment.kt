package com.finalexam.musicboxx.homescreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.ArtistCircleAdapter
import com.finalexam.musicboxx.adapter.MusicSquareAdapter
import com.finalexam.musicboxx.data.repository.ArtistRepository
import com.finalexam.musicboxx.data.repository.SongRepository
import com.finalexam.musicboxx.model.MusicItem
import com.finalexam.musicboxx.model.Song
import com.finalexam.musicboxx.player.PlaySongActivity
import com.finalexam.musicboxx.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class SuggestedFragment : Fragment() {

    private val songRepository by lazy {
        SongRepository(FirebaseFirestore.getInstance())
    }

    private val artistRepository by lazy {
        ArtistRepository(FirebaseFirestore.getInstance())
    }

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
    // SETUP RECYCLERVIEWS
    // ===============================
    private fun setupRecyclerViews(view: View) {

        recentAdapter = MusicSquareAdapter(emptyList()) { musicItem ->
            openPlaySong(musicItem)
        }

        mostPlayedAdapter = MusicSquareAdapter(emptyList()) { musicItem ->
            openPlaySong(musicItem)
        }

        artistAdapter = ArtistCircleAdapter(emptyList()) { }

        view.findViewById<RecyclerView>(R.id.recycler_recently_played).apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = recentAdapter
        }

        view.findViewById<RecyclerView>(R.id.recycler_most_played).apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mostPlayedAdapter
        }

        view.findViewById<RecyclerView>(R.id.recycler_artists).apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = artistAdapter
        }
    }

    // ===============================
    // LOAD DATA
    // ===============================
    private fun loadHomeData() {

        lifecycleScope.launch {
            when (val result = songRepository.getAllSongs()) {
                is Resource.Success -> {
                    recentAdapter.updateData(
                        mapSongsToMusicItems(result.data ?: emptyList())
                    )
                }
                else -> {}
            }
        }

        lifecycleScope.launch {
            when (val result = songRepository.getTrendingSongs()) {
                is Resource.Success -> {
                    mostPlayedAdapter.updateData(
                        mapSongsToMusicItems(result.data ?: emptyList())
                    )
                }
                else -> {}
            }
        }

        lifecycleScope.launch {
            when (val result = artistRepository.getAllArtists()) {
                is Resource.Success -> {
                    artistAdapter.updateData(result.data ?: emptyList())
                }
                else -> {}
            }
        }
    }

    // ===============================
    // NAVIGATION
    // ===============================
    private fun openPlaySong(item: MusicItem) {
        val intent = Intent(requireContext(), PlaySongActivity::class.java)
        intent.putExtra("SONG_ID", item.id)
        intent.putExtra("SONG_TITLE", item.title)
        intent.putExtra("SONG_ARTIST", item.artist)
        startActivity(intent)
    }

    // ===============================
    // MAPPER (QUAN TRỌNG)
    // ===============================
    private fun mapSongsToMusicItems(songs: List<Song>): List<MusicItem> {
        return songs.map {
            MusicItem(
                id = it.id ?: "",
                title = it.title ?: "",
                artist = it.artist ?: "",
                imageResource = R.drawable.ic_launcher_foreground
            )
        }
    }
}
