package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.MusicItem

class MusicSquareAdapter(
    private var musicList: List<MusicItem>,
    private val onItemClick: (MusicItem) -> Unit
) : RecyclerView.Adapter<MusicSquareAdapter.MusicViewHolder>() {

    fun updateData(newList: List<MusicItem>) {
        musicList = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music_square, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val item = musicList[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = musicList.size

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.album_cover)
        private val title: TextView = itemView.findViewById(R.id.song_title)
        private val artist: TextView = itemView.findViewById(R.id.artist_name_small)

        fun bind(item: MusicItem) {
            title.text = item.title
            artist.text = item.artist
            image.setImageResource(item.imageResource)
        }
    }
}
