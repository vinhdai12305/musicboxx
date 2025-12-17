package com.finalexam.musicboxx.homescreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongsAdapter
import com.finalexam.musicboxx.model.Song
import com.finalexam.musicboxx.player.PlaySongActivity

class SongsFragment : Fragment() {

    private lateinit var rvSongs: RecyclerView
    private lateinit var adapter: SongsAdapter

    private val songList = mutableListOf<Song>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_songs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvSongs = view.findViewById(R.id.rvSongs)

        setupRecyclerView()
        loadDummySongs()
    }

    private fun setupRecyclerView() {
        adapter = SongsAdapter(
            songs = songList,

            // 👉 CLICK → PLAY SONG
            onItemClick = { song ->
                openPlaySong(song)
            },

            // 👉 LONG CLICK → OPTIONS
            onItemLongClick = { song ->
                openSongOptions(song)
            }
        )

        rvSongs.layoutManager = LinearLayoutManager(requireContext())
        rvSongs.adapter = adapter
    }

    private fun loadDummySongs() {
        songList.clear()

        songList.addAll(
            listOf(
                Song(
                    id = "1",
                    title = "Không Buông",
                    artist = "Hngle",
                    coverArtRes = R.drawable.mck,
                    durationInSeconds = 215
                ),
                Song(
                    id = "2",
                    title = "3107-3",
                    artist = "W/n",
                    coverArtRes = R.drawable.mck,
                    durationInSeconds = 240
                ),
                Song(
                    id = "3",
                    title = "Thích Em Hơi Nhiều",
                    artist = "Wren Evans",
                    coverArtRes = R.drawable.mck,
                    durationInSeconds = 198
                )
            )
        )

        adapter.notifyDataSetChanged()
    }

    // =========================
    // ACTIONS
    // =========================

    private fun openPlaySong(song: Song) {
        val intent = Intent(requireContext(), PlaySongActivity::class.java).apply {
            putExtra("SONG_DATA", song)
        }
        startActivity(intent)
    }

    private fun openSongOptions(song: Song) {
        val bottomSheet = SongOptionsBottomSheet.newInstance(song)
        bottomSheet.show(childFragmentManager, "SongOptionsBottomSheet")
    }
}
