package com.finalexam.musicboxx.hometab

import Song // Import model Song của bạn

interface SongOptionListener {
    // Hàm quan trọng nhất cho yêu cầu của bạn
    fun onFavoriteClick(song: Song, isFavorite: Boolean)

    // Các hàm cho các nút khác trong menu (để dành dùng sau)
    fun onPlayNext(song: Song)
    fun onAddToQueue(song: Song)
    fun onAddToPlaylist(song: Song)
    fun onGoToAlbum(song: Song)
    fun onGoToArtist(song: Song)
    fun onDeleteSong(song: Song)
}