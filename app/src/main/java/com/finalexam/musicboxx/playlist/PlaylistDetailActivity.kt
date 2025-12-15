package com.aicloudflare.musicbox.PlaylistDetailActivity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.finalexam.musicboxx.R
import androidx.appcompat.app.AppCompatActivity
import com.aicloudflare.musicbox.playlist.AddSongsActivity

class PlaylistDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist_detail)

        // 1. Nhận dữ liệu tên Playlist từ màn hình trước gửi sang
        val playlistName = getIntent().getStringExtra("PLAYLIST_NAME")

        // 2. Tìm và hiển thị tên Playlist
        val tvTitle = findViewById<TextView?>(R.id.tvDetailTitle)
        if (playlistName != null) {
            tvTitle.setText(playlistName)
        }

        // 3. Xử lý sự kiện nút Back (Quay lại)
        findViewById<View?>(R.id.btnBack).setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                finish() // Đóng màn hình này
            }
        })

        // 4. Xử lý sự kiện nút Menu 3 chấm (Góc trên phải)
        val btnMenu = findViewById<ImageView?>(R.id.btnMenu)
        btnMenu.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showPopupMenu(v)
            }
        })

        // 5. Xử lý sự kiện nút Add Box (Dấu cộng hình vuông - Góc phải danh sách)
        // Nút này dùng để mở màn hình thêm bài hát mới
        val btnAddBox = findViewById<ImageView?>(R.id.btnAddBox)
        if (btnAddBox != null) {
            btnAddBox.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    // Chuyển sang màn hình Add Songs
                    val intent: Intent =
                        Intent(this@PlaylistDetailActivity, AddSongsActivity::class.java)
                    startActivity(intent)
                }
            })
        }
    }

    // Hàm hiển thị Menu tùy chọn (Edit/Delete)
    private fun showPopupMenu(view: View?) {
        // Khởi tạo PopupMenu gắn vào view được bấm
        val popup = PopupMenu(this, view)

        // Nạp giao diện từ file menu resource (res/menu/menu_playlist_detail.xml)
        popup.getMenuInflater().inflate(R.menu.menu_playlist_detail, popup.getMenu())

        // Bắt sự kiện khi chọn các mục trong menu
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                val id = item.getItemId()

                if (id == R.id.action_edit) {
                    // Xử lý khi bấm Edit
                    Toast.makeText(
                        this@PlaylistDetailActivity,
                        "Edit Playlist Info",
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                } else if (id == R.id.action_delete) {
                    // Xử lý khi bấm Delete
                    Toast.makeText(
                        this@PlaylistDetailActivity,
                        "Delete Playlist",
                        Toast.LENGTH_SHORT
                    ).show()
                    return true
                }
                return false
            }
        })

        // Hiển thị menu
        popup.show()
    }
}