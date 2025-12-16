package com.finalexam.musicboxx.adapter // Gói của bạn, giữ nguyên

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView // THÊM VÀO
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.MusicItem

// BƯỚC 1: TẠO INTERFACE ĐỂ GIAO TIẾP VỚI FRAGMENT
interface OnSongOptionsClickListener {
    fun onSongOptionsClicked(song: MusicItem)
}

// BƯỚC 2: THAY ĐỔI CONSTRUCTOR ĐỂ NHẬN LISTENER
class SongAdapter(
    private val songList: List<MusicItem>,
    private val listener: OnSongOptionsClickListener // Thêm listener vào constructor
) : RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    // BƯỚC 3: CẬP NHẬT VIEWHOLDER ĐỂ CÓ NÚT 3 CHẤM
    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTitle: TextView = itemView.findViewById(R.id.song_title)
        val songArtist: TextView = itemView.findViewById(R.id.song_artist)
        // Thêm tham chiếu đến ImageView của nút 3 chấm
        val moreOptionsButton: ImageView = itemView.findViewById(R.id.more_options_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        // Đảm bảo bạn đang dùng layout "item_song_list" phiên bản có nút 3 chấm
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_song_list, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentSong = songList[position]
        holder.songTitle.text = currentSong.title
        holder.songArtist.text = currentSong.artist

        // BƯỚC 4: GÁN SỰ KIỆN CLICK CHO NÚT 3 CHẤM
        holder.moreOptionsButton.setOnClickListener {
            // Khi người dùng click, gọi hàm trong interface và truyền bài hát hiện tại
            listener.onSongOptionsClicked(currentSong)
        }
    }
}
