package com.lapperapp.laper.ui.NewDashboard.NewRequest

data class NewRequestSentModel(
    val reqSentDate: Long,
    val expertId: String,
    val reqId: String,
    val expName: String,
    val expImage: String,
    val ps: String,
    val techId: ArrayList<String>
)
