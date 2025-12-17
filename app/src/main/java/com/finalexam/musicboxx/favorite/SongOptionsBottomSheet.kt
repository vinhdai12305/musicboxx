package com.aicloudflare.musicbox.favorite

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

        // ✅ LẤY SONG TỪ BUNDLE (Parcelable)
        song = requireArguments().getParcelable(ARG_SONG)
            ?: error("Song must be passed to SongOptionsBottomSheet")
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

        val tvTitle = view.findViewById<TextView>(R.id.tvSheetTitle)
        val tvInfo = view.findViewById<TextView>(R.id.tvSheetInfo)

        val btnAddToPlaylist =
            view.findViewById<TextView>(R.id.sheetOptionAddToPlaylist)

        val btnDelete =
            view.findViewById<TextView>(R.id.sheetOptionDelete)

        // Hiển thị thông tin bài hát
        tvTitle.text = song.title
        tvInfo.text = song.artist

        btnAddToPlaylist.setOnClickListener {
            Toast.makeText(requireContext(),
                "Thêm '${song.title}' vào Playlist",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }

        btnDelete.setOnClickListener {
            Toast.makeText(requireContext(),
                "Xóa '${song.title}'",
                Toast.LENGTH_SHORT
            ).show()
            dismiss()
        }
    }

    companion object {
        private const val ARG_SONG = "ARG_SONG"

        // ✅ CÁCH DUY NHẤT ĐÚNG ĐỂ TẠO FRAGMENT
        fun newInstance(song: Song): SongOptionsBottomSheet {
            return SongOptionsBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SONG, song)
                }
            }
        }
    }
}
