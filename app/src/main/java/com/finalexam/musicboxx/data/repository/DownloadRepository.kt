package com.finalexam.musicboxx.data.repository

import android.content.Context
import com.finalexam.musicboxx.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

class DownloadRepository(private val context: Context) {

    private val musicDir: File by lazy {
        File(context.filesDir, "music_downloaded").apply { if (!exists()) mkdirs() }
    }

    suspend fun downloadSong(songUrl: String, songId: String): Resource<String> {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(songUrl)
                val connection = url.openConnection()
                connection.connect()
                val inputStream = connection.getInputStream()
                val file = File(musicDir, "$songId.mp3")
                file.outputStream().use { output -> inputStream.copyTo(output) }
                Resource.Success(file.absolutePath)
            } catch (e: Exception) {
                Resource.Error("Lỗi tải: ${e.message}")
            }
        }
    }

    fun isSongDownloaded(songId: String): Boolean = File(musicDir, "$songId.mp3").exists()

    fun getLocalSongPath(songId: String): String? {
        val file = File(musicDir, "$songId.mp3")
        return if (file.exists()) file.absolutePath else null
    }
}