package com.finalexam.musicboxx.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.model.Song
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SongOptionsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var song: Song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Nhận Song từ arguments (Parcelable – chuẩn)
        song = requireArguments().getParcelable(ARG_SONG)
            ?: throw IllegalStateException("Song must not be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.layout_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Header
        val tvTitle = view.findViewById<TextView>(R.id.tvSheetTitle)
        val tvInfo = view.findViewById<TextView>(R.id.tvSheetInfo)

        // Actions
        val btnAddToPlaylist =
            view.findViewById<TextView>(R.id.sheetOptionAddToPlaylist)
        val btnDelete =
            view.findViewById<TextView>(R.id.sheetOptionDelete)

        // Bind data
        tvTitle.text = song.title
        tvInfo.text = song.artist

        // Add to playlist
        btnAddToPlaylist.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Thêm \"${song.title}\" vào Playlist",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }

        // Delete song
        btnDelete.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Xóa \"${song.title}\"",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }
    }

    companion object {
        private const val ARG_SONG = "ARG_SONG"

        fun newInstance(song: Song): SongOptionsBottomSheet {
            return SongOptionsBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SONG, song)
                }
            }
        }
    }
}
