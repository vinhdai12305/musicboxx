package com.finalexam.musicboxx.folders

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FolderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Code này sẽ gắn FolderFragment vào màn hình
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, FolderFragment())
                .commit()
        }
    }
}