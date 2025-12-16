package com.finalexam.musicboxx.albums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R // Import R để lấy id layout

class AlbumAdapter(private val albumList: List<Album>) :
    RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    // 1. Tạo ViewHolder: Giữ các thành phần giao diện
    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAlbum: ImageView = itemView.findViewById(R.id.imgAlbumCover)
        val tvTitle: TextView = itemView.findViewById(R.id.tvAlbumTitle)
        val tvInfo: TextView = itemView.findViewById(R.id.tvArtistYear)
        val tvSongs: TextView = itemView.findViewById(R.id.tvSongCount)
    }

    // 2. Nạp layout item_album.xml vào
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_album, parent, false)
        return AlbumViewHolder(view)
    }

    // 3. Đổ dữ liệu vào từng vị trí
    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albumList[position]

        holder.tvTitle.text = album.title
        // Nối chuỗi: "LYHAN | 2025"
        holder.tvInfo.text = "${album.artist}   |   ${album.year}"
        holder.tvSongs.text = album.songCount
        holder.imgAlbum.setImageResource(album.imageResId)
    }

    // 4. Đếm số lượng phần tử
    override fun getItemCount(): Int {
        return albumList.size
    }
}