package com.finalexam.musicboxx.data.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val favorites: List<String> = emptyList()
)