package com.devianest.u3app.model

data class TelegramUpdate(
    val update_id: Long,
    val message: TelegramMessage?
)