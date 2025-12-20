package com.finalexam.musicboxx.adapter // Đã sửa package cho đúng thư mục

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide // Hết báo đỏ nếu đã Sync Gradle
import com.finalexam.musicboxx.R
import Song

class ArtistsAdapter(
    private var artistsList: List<Song>,
    private val onClick: (Song) -> Unit
) : RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder>() {

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgArtist: ImageView = itemView.findViewById(R.id.ivArtistImage)
        val tvName: TextView = itemView.findViewById(R.id.tvArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        // Nạp layout hình tròn
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artist_circle, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artistsList[position]

        // Hiển thị tên nghệ sĩ
        // Lưu ý: Nếu báo đỏ ở .artist, hãy kiểm tra lại file Song xem tên biến là gì (vd: singer, artistName...)
        holder.tvName.text = artist.artist ?: "Unknown"

        // Load ảnh
        Glide.with(holder.itemView.context)
            // QUAN TRỌNG: Hãy thay 'artist.image' bằng tên đúng trong file Song của bạn
            // Nếu file Song bạn đặt là 'imageUrl' thì giữ nguyên, nếu là 'image' thì sửa lại
            .load(artist.image)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imgArtist)

        holder.itemView.setOnClickListener { onClick(artist) }
    }

    override fun getItemCount(): Int = artistsList.size

    fun updateData(newList: List<Song>) {
        artistsList = newList
        notifyDataSetChanged()
    }
}