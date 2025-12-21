package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.Artist

// Đã sửa lỗi kế thừa ở dòng dưới
class ArtistsHomeTabAdapter(
    private var artistsList: List<Artist>,
    private val onClick: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistsHomeTabAdapter.ArtistViewHolder>() {

    // Class ViewHolder nằm bên trong adapter này
    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgArtist: ImageView = itemView.findViewById(R.id.imgSong)
        val tvName: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDesc: TextView = itemView.findViewById(R.id.tvArtist) // Thêm dòng này để ẩn hoặc hiện text phụ nếu cần
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artist_hometab, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artistsList[position]

        holder.tvName.text = artist.name ?: "Unknown Artist"

        // Ẩn text phụ đi vì item_artist_hometab có 2 dòng text mà Artist chỉ có tên
        // Hoặc bạn có thể set text khác vào đây nếu muốn
        holder.tvDesc.text = "Artist"

        Glide.with(holder.itemView.context)
            .load(artist.image)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imgArtist)

        holder.itemView.setOnClickListener { onClick(artist) }
    }

    override fun getItemCount(): Int = artistsList.size

    fun updateData(newArtists: List<Artist>) {
        artistsList = newArtists
        notifyDataSetChanged()
    }
}