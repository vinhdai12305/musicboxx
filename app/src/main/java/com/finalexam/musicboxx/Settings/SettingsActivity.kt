package com.finalexam.musicboxx.Settings

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.finalexam.musicboxx.R
import com.finalexam.musicboxx.Settings.LanguageActivity
import com.finalexam.musicboxx.Settings.NotificationActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    private lateinit var lblDarkMode: TextView
    private lateinit var tvLanguageValue: TextView     // <--- Thêm dòng này

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cấu hình dark mode trước khi load giao diện
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("DARK_MODE", false)
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(R.layout.activity_settings)

        // Khởi tạo view
        lblDarkMode = findViewById(R.id.lblDarkMode)
        tvLanguageValue = findViewById(R.id.tvLanguageValue)   // <-- Gán view hiển thị ngôn ngữ

        updateDarkModeLabel(isDarkMode)

        setupBottomNavigation()
        setupMenuClicks(isDarkMode)
    }

    // ⭐ CẬP NHẬT NGÔN NGỮ MỖI KHI QUAY LẠI MÀN HÌNH SETTINGS
    override fun onResume() {
        super.onResume()

        val tvLanguage = findViewById<TextView>(R.id.tvLanguageValue)

        val locale = AppCompatDelegate.getApplicationLocales().get(0)
        val lang = locale?.toLanguageTag() ?: "en"

        tvLanguage.text = when {
            lang.startsWith("vi") -> "Vietnamese"
            lang == "en-GB" -> "English (UK)"
            else -> "English (US)"
        }
    }

    private fun setupBottomNavigation() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.selectedItemId = R.id.nav_settings
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home, R.id.nav_favorites, R.id.nav_playlists, R.id.nav_settings -> true
                else -> false
            }
        }
    }

    private fun setupMenuClicks(currentDarkModeState: Boolean) {
        findViewById<View>(R.id.tvNotification).setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        findViewById<View>(R.id.tvLanguage).setOnClickListener {
            startActivity(Intent(this, LanguageActivity::class.java))
        }

        findViewById<View>(R.id.tvDarkMode).setOnClickListener {
            showThemeDialog(getDarkModeState())
        }

        findViewById<View>(R.id.tvQuit).setOnClickListener {
            showQuitDialog()
        }
    }

    private fun updateDarkModeLabel(isDark: Boolean) {
        lblDarkMode.text = if (isDark) getString(R.string.theme_dark)
        else getString(R.string.theme_light)
    }

    private fun getDarkModeState(): Boolean {
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("DARK_MODE", false)
    }

    private fun saveDarkModeState(isDark: Boolean) {
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("DARK_MODE", isDark)
            apply()
        }
    }

    private fun showThemeDialog(isCurrentDark: Boolean) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_theme)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val rgTheme = dialog.findViewById<RadioGroup>(R.id.rgTheme)
        val rbLight = dialog.findViewById<RadioButton>(R.id.rbLight)
        val rbDark = dialog.findViewById<RadioButton>(R.id.rbDark)
        val btnCancel = dialog.findViewById<View>(R.id.btnCancel)
        val btnApply = dialog.findViewById<View>(R.id.btnApply)

        if (isCurrentDark) rbDark.isChecked = true else rbLight.isChecked = true

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnApply.setOnClickListener {
            val isDarkSelected = (rgTheme.checkedRadioButtonId == R.id.rbDark)
            saveDarkModeState(isDarkSelected)
            updateDarkModeLabel(isDarkSelected)

            if (isDarkSelected)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showQuitDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_quit)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val btnCancel = dialog.findViewById<View>(R.id.btnCancel)
        val btnConfirm = dialog.findViewById<View>(R.id.btnConfirmQuit)

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnConfirm.setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }
        dialog.show()
    }
}
