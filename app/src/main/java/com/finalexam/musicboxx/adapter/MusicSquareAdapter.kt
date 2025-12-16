package com.finalexam.musicboxx.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.MusicItem

class MusicSquareAdapter(private val musicList: List<MusicItem>) :
    RecyclerView.Adapter<MusicSquareAdapter.MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        // Sử dụng layout item_music_square.xml
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music_square, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]

        // Gán dữ liệu vào các View
        holder.title.text = music.title
        holder.artist.text = music.artist

        // Gán hình ảnh từ Drawable ID
        holder.image.setImageResource(music.imageResource)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Khai báo các thuộc tính View
        val image: ImageView
        val title: TextView
        val artist: TextView // Đã được khai báo chính xác

        init {
            // Khởi tạo các View bằng ID đã được sửa và thống nhất

            // Image: R.id.album_cover (ID trong item_music_square.xml)
            image = itemView.findViewById(R.id.album_cover)

            // Title: R.id.song_title
            title = itemView.findViewById(R.id.song_title)

            // Artist: R.id.artist_name_small
            artist = itemView.findViewById(R.id.artist_name_small)
        }
    }
}