package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import Song

class FavoritesAdapter(
    private var songsList: List<Song>,
    private val onClick: (Song) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSong: ImageView = itemView.findViewById(R.id.ivSongImage)
        val tvTitle: TextView = itemView.findViewById(R.id.tvSongTitle)
        val tvArtist: TextView = itemView.findViewById(R.id.tvArtistName)
        val btnPlay: ImageView = itemView.findViewById(R.id.btnPlayItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        // Nạp layout item_favorite_song vừa tạo
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_song, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val song = songsList[position]

        holder.tvTitle.text = song.title
        holder.tvArtist.text = song.artist ?: "Unknown"

        // Load ảnh bìa
        Glide.with(holder.itemView.context)
            .load(song.albumArtUrl) // Nhớ dùng đúng tên biến trong Model Song của bạn
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imgSong)

        // Sự kiện click vào item hoặc nút play
        holder.itemView.setOnClickListener { onClick(song) }
        holder.btnPlay.setOnClickListener { onClick(song) }
    }

    override fun getItemCount(): Int = songsList.size

    fun updateData(newList: List<Song>) {
        songsList = newList
        notifyDataSetChanged()
    }
}