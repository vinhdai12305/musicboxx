package com.finalexam.musicboxx.hometab

import Song
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.adapter.SongsListAdapter
import com.finalexam.musicboxx.home.HomeTabViewModel
import com.finalexam.musicboxx.model.Album
import com.google.firebase.firestore.FirebaseFirestore

class AlbumDetailFragment : Fragment(R.layout.fragment_album_detail) {

    private val viewModel: HomeTabViewModel by activityViewModels()
    private lateinit var adapter: SongsListAdapter
    private val albumSongs = mutableListOf<Song>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imgCover: ImageView = view.findViewById(R.id.imgAlbumCover)
        val tvName: TextView = view.findViewById(R.id.tvDetailAlbumName)
        val btnBack: View = view.findViewById(R.id.btnBack)
        val rvAlbumSongs: RecyclerView = view.findViewById(R.id.rvSongs)

        val album = arguments?.getSerializable("album_data") as? Album

        if (album != null) {
            tvName.text = album.name
            Glide.with(this)
                .load(album.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgCover)

            // Setup Adapter
            adapter = SongsListAdapter(
                albumSongs,
                onSongClick = { song ->
                    // --- SỬA LỖI TẠI ĐÂY ---
                    // Thay vì gọi playSong (không tồn tại), ta tìm vị trí bài hát và gọi playUserList
                    // Điều này giúp ExoPlayer biết đang phát Album nào để Next/Prev đúng bài
                    val index = albumSongs.indexOf(song)
                    if (index != -1) {
                        viewModel.playUserList(albumSongs, index)
                    }
                },
                onMoreClick = { song ->
                    Toast.makeText(context, "More: ${song.title}", Toast.LENGTH_SHORT).show()
                }
            )

            rvAlbumSongs.layoutManager = LinearLayoutManager(context)
            rvAlbumSongs.adapter = adapter

            // Tải danh sách bài hát
            fetchSongsByAlbum(album.name)
        } else {
            Toast.makeText(context, "Lỗi: Không nhận được dữ liệu Album", Toast.LENGTH_SHORT).show()
        }

        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun fetchSongsByAlbum(albumName: String) {
        Log.d("AlbumDetail", "Đang tìm bài hát cho album: '$albumName'")

        FirebaseFirestore.getInstance().collection("songs")
            .whereEqualTo("album", albumName)
            .get()
            .addOnSuccessListener { documents ->
                albumSongs.clear()
                for (document in documents) {
                    val song = document.toObject(Song::class.java)
                    song.id = document.id
                    albumSongs.add(song)
                }
                adapter.notifyDataSetChanged()

                if (albumSongs.isEmpty()) {
                    Log.w("AlbumDetail", "Không tìm thấy bài hát nào. Hãy kiểm tra lại tên Album trên Firebase (collection 'songs', field 'album') phải giống hệt: '$albumName'")
                }
            }
            .addOnFailureListener {
                Log.e("AlbumDetail", "Lỗi tải bài hát", it)
            }
    }
}