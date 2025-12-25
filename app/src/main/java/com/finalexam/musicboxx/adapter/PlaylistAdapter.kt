package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.data.model.Playlist

class PlaylistAdapter(
    private val playlists: ArrayList<Playlist>,
    private val onClick: (Playlist) -> Unit,
    private val onDeleteClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.ivPlaylistCover)
        val name: TextView = itemView.findViewById(R.id.tvPlaylistName)
        val artist: TextView = itemView.findViewById(R.id.tvArtist)

        // ⚠️ ĐÚNG ID THEO XML CỦA BẠN
        val more: ImageView = itemView.findViewById(R.id.ivMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]

        // Tên playlist
        holder.name.text = playlist.name

        // Giữ logic cũ: ẩn artist
        holder.artist.visibility = View.GONE

        // Ảnh playlist
        if (playlist.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(playlist.imageUrl)
                .placeholder(R.drawable.img_playlist)
                .error(R.drawable.img_playlist)
                .into(holder.img)
        } else {
            holder.img.setImageResource(R.drawable.img_playlist)
        }

        // Click item → mở playlist detail
        holder.itemView.setOnClickListener {
            onClick(playlist)
        }

        // ================= MENU (3 CHẤM) =================
        holder.more.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)

            // ✅ CHỈ 1 MỤC: DELETE
            popup.menu.add("Delete Playlist")

            popup.setOnMenuItemClickListener { item ->
                if (item.title == "Delete Playlist") {
                    onDeleteClick(playlist)
                    true
                } else {
                    false
                }
            }

            popup.show()
        }
    }

    override fun getItemCount(): Int = playlists.size

    fun updateData(newPlaylists: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
}
