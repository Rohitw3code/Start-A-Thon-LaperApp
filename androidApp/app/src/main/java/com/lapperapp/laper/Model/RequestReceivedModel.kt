package com.lapperapp.laper.Model

data class RequestReceivedModel(
    var desc: String,
    var reqId: String,
    var userId: String,
    var techId: String,
    var reqDate: Long,
    var accepted: Boolean

)
