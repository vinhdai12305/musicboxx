package com.finalexam.musicboxx.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.finalexam.musicboxx.R

class OnboardingActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding2)

        // Bắt sự kiện nút Next ở trang 2
        // (Nếu đây là trang cuối, bấm Next có thể sang màn hình Đăng nhập hoặc Trang chủ)
        val btnNext = findViewById<Button>(R.id.btnNext2)
        btnNext.setOnClickListener {
            // Ví dụ: Bấm Next thì sang màn hình chính (MainActivity)
            val intent = Intent(this, OnboardingActivity3::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}