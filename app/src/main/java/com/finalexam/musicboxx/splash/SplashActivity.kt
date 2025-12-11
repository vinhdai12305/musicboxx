package com.finalexam.musicboxx.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.finalexam.musicboxx.onboarding.OnboardingActivity
import com.finalexam.musicboxx.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 1. Tìm cái hình loading (Đảm bảo trong XML có ImageView id là ivLoading)
        val ivLoading = findViewById<ImageView>(R.id.ivLoading)

        // 2. Nạp hiệu ứng xoay (nếu bạn chưa có file này thì báo mình, mình gửi code XML xoay cho)
        // Code này yêu cầu file res/anim/anim_loading.xml phải tồn tại
        try {
            val rotateAnim = AnimationUtils.loadAnimation(this, R.anim.anim_loading)
            // 3. Bắt đầu xoay
            ivLoading.startAnimation(rotateAnim)
        } catch (e: Exception) {
            // Nếu chưa có file anim thì bỏ qua, không để app bị crash
            e.printStackTrace()
        }

        // 4. Đợi 3 giây rồi chuyển sang màn hình Onboarding
        Handler(Looper.getMainLooper()).postDelayed({
            // --- SỬA LỖI Ở ĐÂY ---
            // Chuyển từ SplashActivity (this) sang OnboardingActivity::class.java
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)

            finish() // Đóng Splash để không quay lại được
        }, 3000) // 3000ms = 3 giây
    }
}