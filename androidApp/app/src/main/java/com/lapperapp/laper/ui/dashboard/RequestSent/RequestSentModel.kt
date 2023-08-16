package com.lapperapp.laper.ui.dashboard.RequestSent

data class RequestSentModel(
    val reqSentDate: Long,
    val expertId: String,
    val reqId: String,
    val expName: String,
    val expImage: String,
    val ps: String,
    val cancel: Boolean,
    val techId: String
)
