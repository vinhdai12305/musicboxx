package com.finalexam.musicboxx.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import Song // Kiểm tra lại dòng import này cho đúng với máy bạn

class SongsAdapter(
    private var list: List<Song>,
    private val onClick: (Song) -> Unit
) : RecyclerView.Adapter<SongsAdapter.Holder>() {

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {
        // QUAN TRỌNG: Đổi ID cho khớp với file item_song_card.xml
        // Nếu trong item_song_card bạn đặt là imgSong thì sửa ở đây là R.id.imgSong
        val img: ImageView = view.findViewById(R.id.imgSong)
        val title: TextView = view.findViewById(R.id.tvTitle)
        val artist: TextView = view.findViewById(R.id.tvArtist)

        init {
            view.setOnClickListener { onClick(list[bindingAdapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // QUAN TRỌNG: Đổi layout thành item_song_card
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_card, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val song = list[position]
        holder.title.text = song.title
        holder.artist.text = song.artist

        Glide.with(holder.itemView.context)
            .load(song.albumArtUrl) // Đảm bảo trong class Song tên biến là albumArtUrl (hoặc imageUrl)
            .placeholder(R.drawable.ic_launcher_background) // Ảnh tạm khi đang load
            .error(android.R.drawable.ic_menu_report_image)
            .into(holder.img)
    }

    override fun getItemCount(): Int = list.size

    // Hàm cập nhật dữ liệu
    fun updateData(newSongs: List<Song>) {
        this.list = newSongs
        notifyDataSetChanged()
    }
}