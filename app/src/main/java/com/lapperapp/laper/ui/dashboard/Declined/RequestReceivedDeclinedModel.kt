package com.lapperapp.laper.ui.dashboard.Declined

data class RequestReceivedDeclinedModel(
    val clientId: String,
    val clientName: String,
    val clientImageUrl: String,
    val requestId: String,
    val desc: String
)
