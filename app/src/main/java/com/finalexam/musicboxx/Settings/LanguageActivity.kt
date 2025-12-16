package com.finalexam.musicboxx.Settings
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.finalexam.musicboxx.R

class LanguageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener { finish() }

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupLanguage)

        // 1. Kiểm tra ngôn ngữ hiện tại để tích đúng chỗ
        val currentLocale = AppCompatDelegate.getApplicationLocales().get(0)
        val langCode = currentLocale?.language ?: "en"

        // Dùng tag để tránh vòng lặp vô tận khi setChecked
        radioGroup.tag = "init"

        when (langCode) {
            "vi" -> radioGroup.check(R.id.rbVietnamese)
            "en" -> {
                if (currentLocale?.country == "GB") radioGroup.check(R.id.rbEnglishUK)
                else radioGroup.check(R.id.rbEnglishUS)
            }
        }
        radioGroup.tag = null // Xong khởi tạo

        // 2. Xử lý sự kiện chọn (CÓ ĐỘ TRỄ CHO MƯỢT)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (group.tag == "init") return@setOnCheckedChangeListener

            // Tạo độ trễ 300ms (0.3 giây) để hiệu ứng tích V chạy xong đã
            Handler(Looper.getMainLooper()).postDelayed({
                when (checkedId) {
                    R.id.rbEnglishUS -> changeAppLanguage("en")
                    R.id.rbEnglishUK -> changeAppLanguage("en-GB")
                    R.id.rbVietnamese -> changeAppLanguage("vi")
                }
            }, 300) // <-- Số 300 này là thời gian chờ (bạn có thể chỉnh lên 500 nếu muốn lâu hơn)
        }
    }

    private fun changeAppLanguage(languageCode: String) {
        val currentLocale = AppCompatDelegate.getApplicationLocales().get(0)
        val currentTag = currentLocale?.toLanguageTag() ?: "en"

        // Chỉ đổi khi ngôn ngữ thực sự khác
        if (!currentTag.startsWith(languageCode)) {
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }
    }
}