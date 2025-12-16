package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.ArtistItem

class ArtistCircleAdapter(
    private var artistList: List<ArtistItem>
) : RecyclerView.Adapter<ArtistCircleAdapter.ArtistViewHolder>() {

    // ===============================
    // UPDATE DATA (QUAN TRỌNG)
    // ===============================
    fun updateData(newList: List<ArtistItem>) {
        artistList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artist_circle, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artistList[position]
        holder.artistName.text = artist.name
        holder.artistImage.setImageResource(artist.imageResource)
    }

    override fun getItemCount(): Int = artistList.size

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val artistImage: ShapeableImageView =
            itemView.findViewById(R.id.item_artist_image)
        val artistName: TextView =
            itemView.findViewById(R.id.item_artist_name)
    }
}
