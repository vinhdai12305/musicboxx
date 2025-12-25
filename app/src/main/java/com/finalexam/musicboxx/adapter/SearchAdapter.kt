package com.finalexam.musicboxx.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import Song // Đảm bảo import đúng model Song của bạn

class SearchAdapter(
    private var list: List<Song>,
    private val onClick: (Song) -> Unit
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgSong)
        val title: TextView = view.findViewById(R.id.tvTitle)
        val artist: TextView = view.findViewById(R.id.tvArtist)

        init {
            view.setOnClickListener { onClick(list[bindingAdapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        // Gắn layout mới vừa tạo (item_search_result)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val song = list[position]
        holder.title.text = song.title
        holder.artist.text = song.artist

        Glide.with(holder.itemView.context)
            .load(song.albumArtUrl) // Hoặc song.imageUrl tuỳ model của bạn
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.img)
    }

    override fun getItemCount(): Int = list.size

    // Hàm cập nhật dữ liệu khi tìm kiếm
    fun updateData(newList: List<Song>) {
        list = newList
        notifyDataSetChanged()
    }
}