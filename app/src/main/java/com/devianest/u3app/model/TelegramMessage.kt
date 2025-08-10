package com.devianest.u3app.model

data class TelegramMessage(
    val message_id: Long,
    val from: TelegramUser?,
    val chat: TelegramChat,
    val date: Long,
    val text: String?
)