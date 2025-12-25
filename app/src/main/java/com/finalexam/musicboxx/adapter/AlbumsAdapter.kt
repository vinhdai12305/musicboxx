package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.Album

class AlbumsAdapter(
    private var albums: List<Album>,
    private val onItemClick: (Album) -> Unit
) : RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAlbum: ImageView = itemView.findViewById(R.id.imgAlbum)
        val tvAlbumName: TextView = itemView.findViewById(R.id.tvAlbumName)
        val tvArtistInfo: TextView = itemView.findViewById(R.id.tvArtistInfo)
        val tvSongCount: TextView = itemView.findViewById(R.id.tvSongCount)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    onItemClick(albums[adapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album_grid, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.tvAlbumName.text = album.name
        holder.tvArtistInfo.text = "${album.artist}"

        // --- SỬA PHẦN NÀY ---
        // Logic: Nếu 0 bài -> "0 songs", 1 bài -> "1 song", nhiều bài -> "N songs"
        val countText = if (album.songCount <= 1) "${album.songCount} song" else "${album.songCount} songs"
        holder.tvSongCount.text = countText

        Glide.with(holder.itemView.context)
            .load(album.imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imgAlbum)
    }

    override fun getItemCount(): Int = albums.size

    fun updateData(newAlbums: List<Album>) {
        albums = newAlbums
        notifyDataSetChanged()
    }
}