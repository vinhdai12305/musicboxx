package com.aicloudflare.musicbox.playlist // Giữ nguyên package của bạn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.aicloudflare.musicbox.PlaylistDetailActivity.PlaylistDetailActivity // Import đúng đường dẫn file chi tiết của bạn
import com.finalexam.musicboxx.R // Import R từ package gốc chứa tài nguyên
import com.google.android.material.button.MaterialButton

class PlaylistActivity : AppCompatActivity() {

    // 1. Khai báo các biến giao diện
    private lateinit var btnBack: View
    private lateinit var layoutAddPlaylist: LinearLayout
    private lateinit var llPlaylistContainer: LinearLayout
    private lateinit var viewDim: View
    private lateinit var containerNewPlaylist: ConstraintLayout
    private lateinit var btnCancel: MaterialButton
    private lateinit var btnCreate: MaterialButton
    private lateinit var etPlaylistName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // QUAN TRỌNG: Sử dụng file layout mà bạn đã sửa giao diện popup
        setContentView(R.layout.activity_create_playlist)

        // 2. Ánh xạ View
        initViews()

        // 3. Thiết lập các sự kiện bấm nút
        setupListeners()

        // 4. Xử lý nút Back (quay lại)
        handleBackPress()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btnBack)
        layoutAddPlaylist = findViewById(R.id.layoutAddPlaylist)

        // Tìm Linear Layout chứa danh sách (Cần đảm bảo bạn đã thêm ID này vào XML như hướng dẫn trước)
        llPlaylistContainer = findViewById(R.id.llPlaylistContainer)

        viewDim = findViewById(R.id.viewDim)
        containerNewPlaylist = findViewById(R.id.containerNewPlaylist)
        btnCancel = findViewById(R.id.btnCancel)
        btnCreate = findViewById(R.id.btnCreate)
        etPlaylistName = findViewById(R.id.etPlaylistName)
    }

    private fun setupListeners() {
        // Nút Back
        btnBack.setOnClickListener {
            finish()
        }

        // Sự kiện: Bấm vào "Add New Playlist" -> Hiện popup
        layoutAddPlaylist.setOnClickListener {
            toggleOverlay(true)
        }

        // Sự kiện: Bấm nút Cancel hoặc vùng tối -> Ẩn popup
        btnCancel.setOnClickListener { toggleOverlay(false) }
        viewDim.setOnClickListener { toggleOverlay(false) }

        // Sự kiện: Bấm nút Create -> Xử lý tạo playlist
        btnCreate.setOnClickListener {
            val playlistName = etPlaylistName.text.toString().trim()

            if (playlistName.isNotEmpty()) {
                // === LOGIC THÊM VIEW MỚI VÀO DANH SÁCH ===

                // 1. Tạo một View mới từ file item_playlist.xml
                // Lưu ý: Đảm bảo bạn có file layout/item_playlist.xml
                val newView = layoutInflater.inflate(R.layout.item_playlist, llPlaylistContainer, false)

                // 2. Tìm TextView trong View mới đó và sửa tên
                val tvName = newView.findViewById<TextView>(R.id.tvPlaylistName)
                val tvSub = newView.findViewById<TextView>(R.id.tvPlaylistSub)

                tvName.text = playlistName
                tvSub.text = "0 songs" // Mặc định playlist mới có 0 bài

                // 3. Chèn View mới vào danh sách (vị trí số 1, ngay dưới nút Add)
                llPlaylistContainer.addView(newView, 1)

                // 4. Thêm sự kiện click cho Playlist vừa tạo để chuyển màn hình
                newView.setOnClickListener {
                    val intent = Intent(this, PlaylistDetailActivity::class.java)
                    intent.putExtra("PLAYLIST_NAME", playlistName)
                    startActivity(intent)
                }

                Toast.makeText(this, "Created: $playlistName", Toast.LENGTH_SHORT).show()

                // Reset ô nhập và ẩn popup
                etPlaylistName.setText("")
                toggleOverlay(false)
            } else {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Hàm ẩn/hiện popup
    private fun toggleOverlay(show: Boolean) {
        if (show) {
            viewDim.visibility = View.VISIBLE
            containerNewPlaylist.visibility = View.VISIBLE
            etPlaylistName.requestFocus() // Focus vào ô nhập
        } else {
            viewDim.visibility = View.GONE
            containerNewPlaylist.visibility = View.GONE
        }
    }

    // Xử lý nút Back của điện thoại
    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Nếu popup đang hiện -> thì ẩn nó đi
                if (containerNewPlaylist.visibility == View.VISIBLE) {
                    toggleOverlay(false)
                } else {
                    // Nếu popup không hiện -> Cho phép thoát màn hình bình thường
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }
}