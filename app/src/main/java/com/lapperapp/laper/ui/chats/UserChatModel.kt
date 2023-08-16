package com.lapperapp.laper.ui.chats

data class UserChatModel(
    val name: String,
    val imageUrl: String,
    val userId: String,
    val lastChatDate: Long,
    val lastChatText: String
)
