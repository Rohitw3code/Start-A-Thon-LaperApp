package com.lapperapp.laper.Notification

data class NotificationModel(
    var imageUrl: String,
    var title: String,
    var text: String,
    var notificationType: String,
    var time: Long,
)

