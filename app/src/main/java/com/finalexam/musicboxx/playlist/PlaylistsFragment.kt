package com.finalexam.musicboxx.playlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager // Đổi sang Linear
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.PlaylistAdapter
import com.finalexam.musicboxx.data.model.Playlist
import java.util.ArrayList

class PlaylistsFragment : Fragment(R.layout.fragment_playlists) {

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvPlaylists = view.findViewById<RecyclerView>(R.id.rvPlaylists)

        // 1. Setup Adapter
        playlistAdapter = PlaylistAdapter(ArrayList()) { playlist ->
            // Click xử lý ở đây
        }
        rvPlaylists.adapter = playlistAdapter

        // 2. QUAN TRỌNG: Đổi thành LinearLayoutManager để giống UI mẫu
        rvPlaylists.layoutManager = LinearLayoutManager(context)

        createFakeData()
    }

    private fun createFakeData() {
        val fakeList = ArrayList<Playlist>()

        // BẮT BUỘC: Thêm item này vào đầu để Adapter nhận diện và thay icon
        fakeList.add(Playlist(id = "ADD_NEW", name = "Add New Playlist"))

        // Các dữ liệu thật
        fakeList.add(Playlist("1", "My Favorite Songs", "", 14))
        fakeList.add(Playlist("2", "16s Old Songs", "", 10))

        playlistAdapter.updateData(fakeList)
    }
}