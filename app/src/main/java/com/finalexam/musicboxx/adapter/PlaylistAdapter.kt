package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.data.model.Playlist

class PlaylistAdapter(
    private var playlists: ArrayList<Playlist>,
    private val onClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.ivPlaylistCover)
        val name: TextView = itemView.findViewById(R.id.tvPlaylistName)
        val count: TextView = itemView.findViewById(R.id.tvArtist) // View này sẽ dùng hiển thị Artist
        val ivMore: ImageView = itemView.findViewById(R.id.ivMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    // Trong Fragment, hàm createFakeData() KHÔNG cần thêm item "ADD_NEW" nữa.
// Chỉ cần thêm playlist thật thôi.

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]

        // Chỉ cần code hiển thị ảnh và tên playlist
        holder.name.text = playlist.name
        holder.count.text = "Hngle, Ari"

        if (playlist.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context).load(playlist.imageUrl).into(holder.img)
        } else {
            holder.img.setImageResource(R.drawable.ic_logo)
        }

        holder.itemView.setOnClickListener { onClick(playlist) }
    }

    override fun getItemCount(): Int = playlists.size

    fun updateData(newPlaylists: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
}