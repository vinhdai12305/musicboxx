package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.ArtistItem
import com.google.android.material.imageview.ShapeableImageView

class ArtistCircleAdapter(
    private var artistList: List<ArtistItem>,
    private val onItemClick: (ArtistItem) -> Unit
) : RecyclerView.Adapter<ArtistCircleAdapter.ArtistViewHolder>() {

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
        holder.bind(artist)
        holder.itemView.setOnClickListener {
            onItemClick(artist)
        }
    }

    override fun getItemCount() = artistList.size

    class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ShapeableImageView =
            itemView.findViewById(R.id.item_artist_image)
        private val name: TextView =
            itemView.findViewById(R.id.item_artist_name)

        fun bind(item: ArtistItem) {
            name.text = item.name
            image.setImageResource(item.imageResource)
        }
    }
}
