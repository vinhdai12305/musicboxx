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
    private var musicList: List<MusicItem>
) : RecyclerView.Adapter<MusicSquareAdapter.MusicViewHolder>() {

    // ===============================
    // HÀM QUAN TRỌNG ĐỂ UPDATE DATA
    // ===============================
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
        val music = musicList[position]

        holder.title.text = music.title
        holder.artist.text = music.artist

        // Hiện tại vẫn dùng drawable (team sẽ đổi sang imageUrl sau)
        holder.image.setImageResource(music.imageResource)
    }

    override fun getItemCount(): Int = musicList.size

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.album_cover)
        val title: TextView = itemView.findViewById(R.id.song_title)
        val artist: TextView = itemView.findViewById(R.id.artist_name_small)
    }
}
