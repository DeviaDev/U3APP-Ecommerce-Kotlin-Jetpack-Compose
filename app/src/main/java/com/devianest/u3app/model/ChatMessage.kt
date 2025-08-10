package com.devianest.u3app.model

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val message: String,
    val isUser: Boolean, // true = user dari aplikasi, false = admin dari telegram
    val timestamp: Long = System.currentTimeMillis(),
    val senderName: String = if (isUser) "User" else "Admin",
    val status: MessageStatus = MessageStatus.SENT
)