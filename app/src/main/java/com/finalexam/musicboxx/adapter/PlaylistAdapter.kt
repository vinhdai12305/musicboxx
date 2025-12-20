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
        val count: TextView = itemView.findViewById(R.id.tvSongCount) // View này sẽ dùng hiển thị Artist
        val ivMore: ImageView = itemView.findViewById(R.id.ivMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]

        if (playlist.id == "ADD_NEW") {
            // --- GIỮ NGUYÊN CODE NÚT ADD BẠN VỪA SỬA ---
            holder.name.text = playlist.name
            holder.count.visibility = View.GONE
            holder.ivMore.visibility = View.GONE

            holder.img.setImageResource(R.drawable.ic_add_circle)
            holder.img.scaleType = ImageView.ScaleType.FIT_CENTER

            // Padding để icon nằm gọn đẹp ở giữa
            val paddingDp = 24
            val density = holder.itemView.context.resources.displayMetrics.density
            val paddingPixel = (paddingDp * density).toInt()
            holder.img.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel)

        } else {
            // --- CẬP NHẬT PHẦN HIỂN THỊ ARTIST ---
            holder.count.visibility = View.VISIBLE
            holder.ivMore.visibility = View.VISIBLE
            holder.name.text = playlist.name

            // --> SỬA Ở ĐÂY: Hiển thị tên Artist
            // Nếu bạn muốn hiện cả artist và số bài hát thì dùng: "${playlist.artist} • ${playlist.songCount}"
            holder.count.text = playlist.artist

            // Reset lại ảnh thường
            holder.img.scaleType = ImageView.ScaleType.CENTER_CROP
            holder.img.setPadding(0, 0, 0, 0) // Quan trọng: Xóa padding

            if (playlist.imageUrl.isNotEmpty()) {
                Glide.with(holder.itemView.context)
                    .load(playlist.imageUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(holder.img)
            } else {
                holder.img.setImageResource(R.drawable.ic_launcher_background)
            }
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