package com.finalexam.musicboxx.adapter

import android.graphics.Color
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
    private var playlists: List<Playlist>,
    private val onClick: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.ivPlaylistCover)
        val name: TextView = itemView.findViewById(R.id.tvPlaylistName)
        val count: TextView = itemView.findViewById(R.id.tvSongCount)
        val ivMore: ImageView = itemView.findViewById(R.id.ivMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_playlist, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]

        // Ánh xạ CardView từ itemView
        val cardView = holder.itemView.findViewById<androidx.cardview.widget.CardView>(R.id.cardImg)

        if (playlist.id == "ADD_NEW") {
            // --- 1. RIÊNG NÚT ADD: HÌNH TRÒN TUYỆT ĐỐI ---
            // Với khung 82dp, radius = 41dp sẽ tạo hình tròn
            cardView.radius = 41f * holder.itemView.context.resources.displayMetrics.density

            holder.name.text = "Add New Playlist"
            holder.count.visibility = View.GONE // Ẩn dòng artist
            holder.ivMore.visibility = View.GONE // Ẩn nút 3 chấm

            // Load icon add và chỉnh màu cam nhạt như thiết kế
            holder.img.setImageResource(R.drawable.ic_add_circle) // Thay bằng tên icon add của bạn
            holder.img.setPadding(0, 0, 0, 0)

        } else {
            // --- 2. PLAYLIST THƯỜNG: HÌNH VUÔNG BO GÓC ---
            // Trả về độ bo góc vuông giống các trang trước (ví dụ 16dp)
            cardView.radius = 16f * holder.itemView.context.resources.displayMetrics.density

            holder.count.visibility = View.VISIBLE
            holder.ivMore.visibility = View.VISIBLE
            holder.name.text = playlist.name
            holder.count.text = "Hngle, Ari" // Hoặc dữ liệu từ playlist

            // Reset các thiết lập của nút Add
            holder.img.setPadding(0, 0, 0, 0)
            holder.img.background = null
            holder.img.clearColorFilter()
            holder.img.scaleType = ImageView.ScaleType.CENTER_CROP // Playlist thường thì hiện full ảnh

            if (playlist.imageUrl.isNotEmpty()) {
                Glide.with(holder.itemView.context).load(playlist.imageUrl).into(holder.img)
            } else {
                holder.img.setImageResource(R.drawable.img_onboarding1)
            }
        }

        holder.itemView.setOnClickListener { onClick(playlist) }
    }

    override fun getItemCount(): Int = playlists.size

    fun updateData(newList: List<Playlist>) {
        playlists = newList
        notifyDataSetChanged()
    }
}