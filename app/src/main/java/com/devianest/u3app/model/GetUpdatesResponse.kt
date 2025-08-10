package com.devianest.u3app.model

data class GetUpdatesResponse(
    val ok: Boolean,
    val result: List<TelegramUpdate>
)