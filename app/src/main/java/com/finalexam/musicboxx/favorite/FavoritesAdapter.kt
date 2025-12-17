package com.finalexam.musicboxx.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.homescreen.SongOptionsBottomSheet
import com.finalexam.musicboxx.model.Song

class FavoritesAdapter(
    private var songList: List<Song>,
    private val onItemClick: (Song) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_song_simple, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val song = songList[position]
        holder.bind(song)

        // CLICK → PLAY
        holder.itemView.setOnClickListener {
            onItemClick(song)
        }

        // LONG CLICK → OPTIONS
        holder.itemView.setOnLongClickListener {
            SongOptionsBottomSheet
                .newInstance(song)
                .show(
                    (holder.itemView.context as AppCompatActivity)
                        .supportFragmentManager,
                    "FavoriteSongOptions"
                )
            true
        }
    }

    override fun getItemCount(): Int = songList.size

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.song_title)
        private val artist: TextView = itemView.findViewById(R.id.artist_name)
        private val cover: ImageView = itemView.findViewById(R.id.album_cover)

        fun bind(song: Song) {
            title.text = song.title
            artist.text = song.artist
            cover.setImageResource(song.coverArtRes)
        }
    }
}
