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
    private var albums: List<Album>
) : RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAlbum: ImageView = itemView.findViewById(R.id.imgAlbum)
        val tvAlbumName: TextView = itemView.findViewById(R.id.tvAlbumName)
        val tvArtist: TextView = itemView.findViewById(R.id.tvAlbumArtist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_album_grid, parent, false) // Dùng layout ô vuông
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.tvAlbumName.text = album.name
        holder.tvArtist.text = album.artist

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