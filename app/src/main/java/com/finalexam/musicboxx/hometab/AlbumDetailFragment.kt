package com.finalexam.musicboxx.hometab

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.Album

class AlbumDetailFragment : Fragment(R.layout.fragment_album_detail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Ánh xạ View
        val imgCover: ImageView = view.findViewById(R.id.imgAlbumCover)
        val tvName: TextView = view.findViewById(R.id.tvDetailAlbumName)
        val btnBack: View = view.findViewById(R.id.btnBack)

        // 2. Nhận dữ liệu từ Bundle
        val album = arguments?.getSerializable("album_data") as? Album

        // 3. Hiển thị dữ liệu
        if (album != null) {
            tvName.text = album.name
            Glide.with(this).load(album.imageUrl).into(imgCover)
        }

        // 4. Xử lý nút Back
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // TODO: Tại đây bạn sẽ setup RecyclerView cho danh sách bài hát (rvSongs) sau này
    }
}