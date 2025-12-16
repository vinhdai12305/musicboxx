package com.finalexam.musicboxx.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.finalexam.musicboxx.R

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // Tìm nút Next ở trang 1
        val btnNext = findViewById<Button>(R.id.btnNext)

        // Bắt sự kiện click
        btnNext.setOnClickListener {
            // Chuyển sang trang 2 (OnboardingActivity2)
            val intent = Intent(this, OnboardingActivity2::class.java)
            startActivity(intent)

            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}