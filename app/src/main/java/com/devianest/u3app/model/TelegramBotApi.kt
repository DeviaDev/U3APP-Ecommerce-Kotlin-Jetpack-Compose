package com.devianest.u3app.model

import retrofit2.Response
import retrofit2.http.*

interface TelegramBotApi {
    @GET("getUpdates")
    suspend fun getUpdates(
        @Query("offset") offset: Long? = null,
        @Query("timeout") timeout: Int = 30
    ): Response<GetUpdatesResponse>

    @POST("sendMessage")
    suspend fun sendMessage(
        @Query("chat_id") chatId: Long,
        @Query("text") text: String
    ): Response<TelegramResponse>
}