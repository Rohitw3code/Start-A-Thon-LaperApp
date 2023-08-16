package com.lapperapp.laper.ui.dashboard.AvailableClient

data class AvailableClientModel(
    val userName: String,
    val userImageUrl: String,
    val approvalDate: Long,
    val userId: String,
    val requestId: String,
    val ps: String,
    val techId: String
)
