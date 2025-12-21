package com.finalexam.musicboxx.setting

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.finalexam.musicboxx.MainActivity
import com.finalexam.musicboxx.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var tvCurrentLanguage: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvCurrentLanguage = view.findViewById(R.id.tvCurrentLanguage)

        // 1. Xử lý Language
        view.findViewById<View>(R.id.optLanguage).setOnClickListener {
            showLanguageDialog()
        }

        // 2. Xử lý các nút khác (Chỉ hiện thông báo)
        setupClick(view, R.id.optBackup, "Backup")
        setupClick(view, R.id.optNotification, "Notification")
        setupClick(view, R.id.optChangeLog, "Change Log")
        setupClick(view, R.id.optFAQ, "FAQ")

        // 3. Xử lý nút Quit (Thoát App hoặc Đăng xuất)
        view.findViewById<View>(R.id.optQuit).setOnClickListener {
            Toast.makeText(context, "Quitting...", Toast.LENGTH_SHORT).show()
            // Code thoát app hoặc đăng xuất
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun setupClick(view: View, id: Int, name: String) {
        view.findViewById<View>(id).setOnClickListener {
            Toast.makeText(context, "Clicked: $name", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLanguageDialog() {
        val languages = arrayOf("English (US)", "Vietnamese (Tiếng Việt)", "Spanish", "Japanese")
        AlertDialog.Builder(requireContext())
            .setTitle("Select Language")
            .setItems(languages) { _, which ->
                tvCurrentLanguage.text = languages[which]
            }
            .show()
    }
}