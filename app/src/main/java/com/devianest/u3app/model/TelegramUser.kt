package com.devianest.u3app.model

data class TelegramUser(
    val id: Long,
    val first_name: String,
    val last_name: String?,
    val username: String?
)