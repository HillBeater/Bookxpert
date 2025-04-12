package com.hillbeater.bookxpert.model

data class FCMRequest(
    val message: Message
)

data class Message(
    val token: String,
    val data: Map<String, String>? = null
)
