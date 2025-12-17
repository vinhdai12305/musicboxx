package com.finalexam.musicboxx.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R

class OnboardingActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding3)

        val btnNext = findViewById<Button>(R.id.btnNext3)

        btnNext.setOnClickListener {
            // ĐÂY LÀ BƯỚC QUAN TRỌNG: Chuyển vào Màn hình chính (Home)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            // Hiệu ứng chuyển trang mượt
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            // Đóng toàn bộ luồng onboarding để user không back lại được
            finishAffinity()
        }
    }
}