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

class ArtistsHomeTabAdapter(
    private var artistsList: List<Artist>,
    private val onClick: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistsHomeTabAdapter.ArtistViewHolder>() {

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgArtist: ImageView = itemView.findViewById(R.id.imgArtist)
        val tvArtistName: TextView = itemView.findViewById(R.id.tvArtistName)
        val tvArtistInfo: TextView = itemView.findViewById(R.id.tvArtistInfo)
        val btnMore: ImageView = itemView.findViewById(R.id.btnMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artist_hometab, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artistsList[position]

        holder.tvArtistName.text =
            if (artist.name.isNotBlank()) artist.name else "Unknown Artist"

        // Vì model chỉ có name + image
        holder.tvArtistInfo.text = "Artist"

        Glide.with(holder.itemView.context)
            .load(artist.image)
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imgArtist)

        holder.itemView.setOnClickListener { onClick(artist) }

        holder.btnMore.setOnClickListener {
            // TODO: popup menu sau
        }
    }

    override fun getItemCount(): Int = artistsList.size

    fun updateData(newArtists: List<Artist>) {
        artistsList = newArtists
        notifyDataSetChanged()
    }
}
