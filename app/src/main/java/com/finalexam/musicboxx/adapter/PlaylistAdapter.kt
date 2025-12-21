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
        // Đây là view hiển thị Artist/Count mà bạn muốn ẩn
        val count: TextView = itemView.findViewById(R.id.tvArtist)
        val ivMore: ImageView = itemView.findViewById(R.id.ivMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]

        // 1. Hiển thị Tên Playlist
        holder.name.text = playlist.name

        // 2. ẨN HOÀN TOÀN dòng Artist Name (Quan trọng)
        // View.GONE: Ẩn đi và không chiếm chỗ trống trên màn hình
        holder.count.visibility = View.GONE

        // 3. Hiển thị ảnh (Sử dụng Glide)
        if (playlist.imageUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(playlist.imageUrl)
                .placeholder(R.drawable.ic_logo) // Ảnh hiển thị khi đang load (tùy chọn)
                .error(R.drawable.ic_logo)       // Ảnh hiển thị khi link lỗi
                .into(holder.img)
        } else {
            // Nếu không có link ảnh thì hiện ảnh logo mặc định
            holder.img.setImageResource(R.drawable.ic_logo)
        }

        // 4. Bắt sự kiện click vào playlist
        holder.itemView.setOnClickListener { onClick(playlist) }
    }

    override fun getItemCount(): Int = playlists.size

    // Hàm cập nhật dữ liệu mới (nếu cần dùng sau này)
    fun updateData(newPlaylists: List<Playlist>) {
        playlists.clear()
        playlists.addAll(newPlaylists)
        notifyDataSetChanged()
    }
}