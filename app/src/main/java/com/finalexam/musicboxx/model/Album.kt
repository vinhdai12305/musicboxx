package com.finalexam.musicboxx.model
import java.io.Serializable
data class Album(
    val id: String = "",
    val name: String = "",
    val artist: String = "",
    val imageUrl: String = ""
) : Serializable