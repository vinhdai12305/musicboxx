package com.finalexam.musicboxx.home

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R

// Thêm thư viện này để dùng các hằng số REPEAT_MODE
import androidx.media3.common.Player

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.concurrent.TimeUnit

class NowPlayingFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: HomeTabViewModel
    private lateinit var sbProgress: SeekBar
    private lateinit var tvCurrent: TextView
    private lateinit var tvTotal: TextView

    // Handler để cập nhật seekbar liên tục
    private val handler = Handler(Looper.getMainLooper())
    private val updateTask = object : Runnable {
        override fun run() {
            updateSeekBar()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_now_playing, container, false)
    }

    // --- 1. ĐÃ SỬA ĐỂ FULL MÀN HÌNH (MATCH_PARENT) ---
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)

                // Mới thêm: Ép chiều cao layout bằng chiều cao màn hình
                val layoutParams = sheet.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                sheet.layoutParams = layoutParams

                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                behavior.skipCollapsed = true
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Lấy ViewModel chung của Activity
        viewModel = ViewModelProvider(requireActivity())[HomeTabViewModel::class.java]

        // Ánh xạ View
        val imgArt: ImageView = view.findViewById(R.id.imgArtwork)
        val tvTitle: TextView = view.findViewById(R.id.tvSongTitle)
        val tvArtist: TextView = view.findViewById(R.id.tvArtistName)
        val btnPlay: ImageView = view.findViewById(R.id.btnPlayPause)
        val btnNext: ImageView = view.findViewById(R.id.btnNext)
        val btnPrev: ImageView = view.findViewById(R.id.btnPrev)
        val btnShuffle: ImageView = view.findViewById(R.id.btnShuffle)
        val btnRepeat: ImageView = view.findViewById(R.id.btnRepeat)
        val btnBack: ImageView = view.findViewById(R.id.btnBack)

        sbProgress = view.findViewById(R.id.seekBar)
        tvCurrent = view.findViewById(R.id.tvCurrentTime)
        tvTotal = view.findViewById(R.id.tvTotalTime)

        // --- SỰ KIỆN CLICK ---
        btnBack.setOnClickListener { dismiss() } // Tắt màn hình
        btnPlay.setOnClickListener { viewModel.togglePlayPause() }
        btnNext.setOnClickListener { viewModel.skipNext() }
        btnPrev.setOnClickListener { viewModel.skipPrev() }
        btnShuffle.setOnClickListener { viewModel.toggleShuffle() }
        btnRepeat.setOnClickListener { viewModel.toggleRepeat() }

        // Seekbar change
        sbProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) tvCurrent.text = formatTime(progress.toLong())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar?.let { viewModel.seekTo(it.progress.toLong()) }
            }
        })

        // --- QUAN SÁT DỮ LIỆU ---

        // 1. Bài hát hiện tại
        viewModel.currentSong.observe(viewLifecycleOwner) { song ->
            if (song != null) {
                tvTitle.text = song.title
                tvArtist.text = song.artist
                Glide.with(this).load(song.albumArtUrl).into(imgArt)

                // Reset seekbar max
                sbProgress.max = viewModel.getDuration().toInt()
                tvTotal.text = formatTime(viewModel.getDuration())
            }
        }

        // 2. Trạng thái Play/Pause
        viewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            btnPlay.setImageResource(if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play)
            if (isPlaying) handler.post(updateTask) else handler.removeCallbacks(updateTask)
        }

        // 3. Shuffle (Đổi màu cam/xám)
        viewModel.isShuffle.observe(viewLifecycleOwner) { isShuffle ->
            val color = if (isShuffle) Color.parseColor("#FF6D00") else Color.parseColor("#757575")
            btnShuffle.setColorFilter(color)
        }

        // --- 2. ĐÃ THÊM LOGIC NÚT LẶP BÀI HÁT ---
        viewModel.repeatMode.observe(viewLifecycleOwner) { mode ->
            when (mode) {
                Player.REPEAT_MODE_OFF -> {
                    // Tắt lặp: Màu xám, Icon lặp thường
                    btnRepeat.setColorFilter(Color.parseColor("#757575"))
                    btnRepeat.setImageResource(R.drawable.ic_repeat)
                }
                Player.REPEAT_MODE_ALL -> {
                    // Lặp danh sách: Màu cam, Icon lặp thường
                    btnRepeat.setColorFilter(Color.parseColor("#FF6D00"))
                    btnRepeat.setImageResource(R.drawable.ic_repeat)
                }
                Player.REPEAT_MODE_ONE -> {
                    // Lặp 1 bài: Màu cam, Icon lặp số 1
                    btnRepeat.setColorFilter(Color.parseColor("#FF6D00"))
                    // Nếu bạn có icon ic_repeat_one thì dùng, nếu không thì dùng tạm ic_repeat
                    btnRepeat.setImageResource(R.drawable.ic_repeat)
                }
            }
        }
    }

    private fun updateSeekBar() {
        val current = viewModel.getCurrentPosition()
        sbProgress.progress = current.toInt()
        tvCurrent.text = formatTime(current)
    }

    private fun formatTime(ms: Long): String {
        return String.format("%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(ms),
            TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateTask)
    }
}