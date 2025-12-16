package com.finalexam.musicboxx.homescreen

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.finalexam.musicboxx.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // ==========================================
        // 1. XỬ LÝ THANH MENU DƯỚI ĐÁY
        // ==========================================
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.navigation_settings
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home, R.id.navigation_favorites, R.id.navigation_playlists, R.id.navigation_settings -> true
                else -> false
            }
        }

        // ==========================================
        // 2. XỬ LÝ CHUYỂN TRANG NOTIFICATION
        // ==========================================
        val btnNotification = findViewById<View>(R.id.tvNotification)
        btnNotification.setOnClickListener {
            val intent = Intent(this, NotificationActivities::class.java)
            startActivity(intent)
        }

        // ==========================================
        // 3. XỬ LÝ CHUYỂN TRANG LANGUAGE
        // ==========================================
        val btnLanguage = findViewById<View>(R.id.tvLanguage)
        btnLanguage.setOnClickListener {
            val intent = Intent(this, LanguageActivities::class.java)
            startActivity(intent)
        }

        // ==========================================
        // 4. XỬ LÝ NÚT QUIT (HIỆN DIALOG) - MỚI THÊM
        // ==========================================
        val btnQuit = findViewById<View>(R.id.tvQuit)
        btnQuit.setOnClickListener {
            showQuitDialog()
        }
    }

    // Hàm hiển thị Dialog hỏi thoát
    private fun showQuitDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.log_quit)

        // Làm nền trong suốt để bo góc đẹp
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Chỉnh chiều rộng dialog cho đẹp (chiếm toàn chiều ngang)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Xử lý nút bấm trong Dialog
        val btnCancel = dialog.findViewById<View>(R.id.btnCancel)
        val btnConfirm = dialog.findViewById<View>(R.id.btnConfirmQuit)

        btnCancel.setOnClickListener {
            dialog.dismiss() // Đóng dialog
        }

        btnConfirm.setOnClickListener {
            dialog.dismiss()
            finishAffinity() // Thoát hẳn ứng dụng
        }

        dialog.show()
    }
}