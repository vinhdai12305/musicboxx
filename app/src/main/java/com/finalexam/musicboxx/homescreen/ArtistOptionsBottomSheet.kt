package com.finalexam.musicboxx.homescreen // Hoặc package tương ứng của bạn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.ArtistItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ArtistOptionsBottomSheet : BottomSheetDialogFragment() {

    // Companion object để tạo instance mới một cách an toàn
    companion object {
        private const val ARG_ARTIST = "artist_item"

        fun newInstance(artist: ArtistItem): ArtistOptionsBottomSheet {
            val fragment = ArtistOptionsBottomSheet()
            val args = Bundle().apply {
                putParcelable(ARG_ARTIST, artist) // Yêu cầu ArtistItem phải là Parcelable
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Nạp layout cho Bottom Sheet
        return inflater.inflate(R.layout.bottom_sheet_artist_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val artist: ArtistItem? = arguments?.getParcelable(ARG_ARTIST)

        // Chỉ thực hiện khi có dữ liệu artist
        artist?.let { currentArtist ->
            // Ánh xạ các View và gán dữ liệu
            view.findViewById<ImageView>(R.id.artist_image_bottom_sheet)?.setImageResource(currentArtist.imageResource)
            view.findViewById<TextView>(R.id.artist_name_bottom_sheet)?.text = currentArtist.name
            view.findViewById<TextView>(R.id.artist_stats_bottom_sheet)?.text = "${currentArtist.albumCount} Album  |  ${currentArtist.songCount} Songs"

            // Gán sự kiện click cho các tùy chọn
            view.findViewById<TextView>(R.id.option_play)?.setOnClickListener {
                showToast("Play tracks by ${currentArtist.name}")
                dismiss() // Đóng Bottom Sheet
            }
            view.findViewById<TextView>(R.id.option_play_next)?.setOnClickListener {
                showToast("Play next: ${currentArtist.name}")
                dismiss()
            }
            // ... các listener khác
        }
    }

    private fun showToast(message: String) {
        context?.let { Toast.makeText(it, message, Toast.LENGTH_SHORT).show() }
    }
}
