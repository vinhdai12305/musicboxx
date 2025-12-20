package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.Artist // <--- IMPORT ĐÚNG FILE MODEL VỪA TẠO

class ArtistsAdapter(
    private var artistsList: List<Artist>, // Nhận vào List<Artist>
    private val onClick: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder>() {

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgArtist: ImageView = itemView.findViewById(R.id.ivArtistImage)
        val tvName: TextView = itemView.findViewById(R.id.tvArtistName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artist_circle, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artistsList[position]
        holder.tvName.text = artist.name ?: "Unknown"

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