package com.finalexam.musicboxx.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.activity.ArtistDetailActivity
import com.finalexam.musicboxx.model.ArtistItem

// Sửa đổi Adapter để nhận thêm 1 lambda cho việc click vào item
class ArtistAdapter(
    private val artistList: List<ArtistItem>,
    private val onOptionClick: (ArtistItem) -> Unit,
    private val onItemClick: (ArtistItem) -> Unit // Lambda mới cho click vào item
) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ... (giữ nguyên không đổi)
        val artistImage: ImageView = itemView.findViewById(R.id.artist_image)
        val artistName: TextView = itemView.findViewById(R.id.artist_name)
        val albumCount: TextView = itemView.findViewById(R.id.album_count)
        val songCount: TextView = itemView.findViewById(R.id.song_count)
        val optionsMenu: ImageView = itemView.findViewById(R.id.options_menu)

        // BẮT SỰ KIỆN CLICK VÀO TOÀN BỘ ITEM
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(artistList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        // ... (giữ nguyên không đổi)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_artist, parent, false)
        return ArtistViewHolder(view)
    }

    override fun getItemCount(): Int = artistList.size

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        // ... (giữ nguyên không đổi)
        val currentArtist = artistList[position]
        holder.artistName.text = currentArtist.name
        holder.artistImage.setImageResource(currentArtist.imageResource)
        holder.albumCount.text = "${currentArtist.albumCount} Albums"
        holder.songCount.text = "${currentArtist.songCount} Songs"

        holder.optionsMenu.setOnClickListener {
            onOptionClick(currentArtist)
        }
    }
}
