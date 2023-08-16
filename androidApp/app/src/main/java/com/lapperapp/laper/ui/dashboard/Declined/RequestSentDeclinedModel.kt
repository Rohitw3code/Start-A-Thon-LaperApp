package com.lapperapp.laper.ui.dashboard.Declined

data class RequestSentDeclinedModel(
    val expertId: String,
    val expertName: String,
    val expertImageUrl: String,
    val requestId: String,
    val desc: String
)
