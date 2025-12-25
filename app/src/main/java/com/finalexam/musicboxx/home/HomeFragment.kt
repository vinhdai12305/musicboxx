package com.finalexam.musicboxx.home

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.finalexam.musicboxx.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeFragment : Fragment(R.layout.fragment_home) {

    // Khai báo ViewModel để lấy dữ liệu bài hát
    private lateinit var viewModel: HomeTabViewModel

    // Khai báo các view của Mini Player
    private lateinit var layoutMiniPlayer: ConstraintLayout
    private lateinit var imgMiniCover: ImageView
    private lateinit var tvMiniTitle: TextView
    private lateinit var tvMiniArtist: TextView
    private lateinit var btnMiniPlay: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ================= 1. SETUP NAVIGATION (Code cũ) =================
        val bottomNav = view.findViewById<BottomNavigationView>(R.id.bottomNav)
        val navHostFragment = childFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment
        bottomNav.setupWithNavController(navHostFragment.navController)

        // ================= 2. SETUP MINI PLAYER (Code mới) =================

        // A. Kết nối ViewModel (Dùng chung với Activity)
        viewModel = ViewModelProvider(requireActivity())[HomeTabViewModel::class.java]

        // B. Ánh xạ View (Tìm các view trong layout bạn vừa thêm)
        layoutMiniPlayer = view.findViewById(R.id.layoutMiniPlayer)
        imgMiniCover = view.findViewById(R.id.imgMiniCover)
        tvMiniTitle = view.findViewById(R.id.tvMiniTitle)
        tvMiniArtist = view.findViewById(R.id.tvMiniArtist)
        btnMiniPlay = view.findViewById(R.id.btnMiniPlay)

        // C. Lắng nghe bài hát hiện tại (Logic quan trọng để HIỆN Player)
        viewModel.currentSong.observe(viewLifecycleOwner) { song ->
            if (song != null) {
                // -> CÓ BÀI HÁT: HIỆN MINI PLAYER
                layoutMiniPlayer.visibility = View.VISIBLE

                // Cập nhật tên và ảnh
                tvMiniTitle.text = song.title
                tvMiniArtist.text = song.artist

                // Load ảnh (Dùng Glide)
                // Lưu ý: Đảm bảo biến chứa link ảnh trong model Song là 'imageUrl' hoặc 'albumArtUrl'
                // Ở đây mình ví dụ là 'imageUrl', bạn sửa lại nếu khác nhé
                Glide.with(this)
                    .load(song.albumArtUrl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imgMiniCover)

            } else {
                // -> KHÔNG CÓ BÀI HÁT: ẨN ĐI
                layoutMiniPlayer.visibility = View.GONE
            }
        }

        // D. Lắng nghe trạng thái Play/Pause để đổi icon nút nhỏ
        viewModel.isPlaying.observe(viewLifecycleOwner) { isPlaying ->
            if (isPlaying) {
                btnMiniPlay.setImageResource(R.drawable.ic_pause) // Đổi thành nút Pause
            } else {
                btnMiniPlay.setImageResource(R.drawable.ic_play) // Đổi thành nút Play
            }
        }

        // ================= 3. XỬ LÝ CLICK =================

        // Bấm nút Play/Pause nhỏ
        btnMiniPlay.setOnClickListener {
            viewModel.togglePlayPause()
        }

        // Bấm vào thanh Mini Player -> Mở Player to (NowPlayingFragment)
        layoutMiniPlayer.setOnClickListener {
            val nowPlayingDialog = NowPlayingFragment()
            nowPlayingDialog.show(parentFragmentManager, "NowPlaying")
        }
    }
}