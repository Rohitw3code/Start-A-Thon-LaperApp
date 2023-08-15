package com.lapperapp.laper.ui.dashboard.TaskComplete

data class TaskCompleteModel(
    val expertId: String,
    val expertName: String,
    val expertImageUrl: String,
    val requestId: String,
    val ps: String,
    val time: Long,
    val techId: String
)
