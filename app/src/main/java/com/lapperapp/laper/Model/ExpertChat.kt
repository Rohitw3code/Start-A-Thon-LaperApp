package com.lapperapp.laper.Model

data class ExpertChat(
    val expertId:String,
    val name:String,
    val imageUrl:String,
    val freeze:Boolean,
    val timestamp:Long,
    val lastMessage:String
)
