package com.biturver.app.feature.chat.presentation

data class ChatMessage(
    val message: String,
    val isUser: Boolean,
    val targetActivity: Class<*>? = null
)