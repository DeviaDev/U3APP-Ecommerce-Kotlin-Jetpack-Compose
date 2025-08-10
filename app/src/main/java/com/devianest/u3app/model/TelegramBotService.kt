package com.devianest.u3app.model

import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.util.Log
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "chat_settings")

class TelegramBotService(private val context: Context) {
    private val botToken = "7638471670:AAG0qqR4KmY50RQf8zvxILafzFKerFtfD9E"
    private val baseUrl = "https://api.telegram.org/bot$botToken/"

    // Chat ID admin yang akan menerima pesan (harus diset manual)
    private val ADMIN_CHAT_ID_KEY = longPreferencesKey("admin_chat_id")
    private val LAST_UPDATE_ID_KEY = longPreferencesKey("last_update_id")

    private val api = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TelegramBotApi::class.java)

    private var isPolling = false

    // Simpan admin chat ID (dipanggil saat admin pertama kali chat ke bot)
    suspend fun setAdminChatId(chatId: Long) {
        context.dataStore.edit { preferences ->
            preferences[ADMIN_CHAT_ID_KEY] = chatId
        }
    }

    // Dapatkan admin chat ID
    private suspend fun getAdminChatId(): Long? {
        return context.dataStore.data.map { preferences ->
            preferences[ADMIN_CHAT_ID_KEY]
        }.first()
    }

    // Kirim pesan dari user aplikasi ke admin telegram
    suspend fun sendUserMessageToAdmin(userMessage: String, userName: String = "User"): Boolean {
        return try {
            val adminChatId = getAdminChatId()
            if (adminChatId != null) {
                val formattedMessage = "💬 Pesan dari $userName:\n\n$userMessage\n\n⏰ ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(java.util.Date())}"
                val response = api.sendMessage(adminChatId, formattedMessage)
                response.isSuccessful
            } else {
                Log.e("TelegramBot", "Admin chat ID not set")
                false
            }
        } catch (e: Exception) {
            Log.e("TelegramBot", "Error sending message to admin", e)
            false
        }
    }

    // Mulai polling untuk menerima balasan admin
    fun startPolling(onAdminReply: (String) -> Unit, onAdminRegistered: (Long) -> Unit) {
        if (isPolling) return
        isPolling = true

        CoroutineScope(Dispatchers.IO).launch {
            var lastUpdateId = context.dataStore.data.map { preferences ->
                preferences[LAST_UPDATE_ID_KEY] ?: 0L
            }.first()

            while (isPolling) {
                try {
                    val response = api.getUpdates(lastUpdateId + 1, 30)
                    if (response.isSuccessful) {
                        response.body()?.result?.forEach { update ->
                            lastUpdateId = update.update_id

                            // Simpan last update ID
                            context.dataStore.edit { preferences ->
                                preferences[LAST_UPDATE_ID_KEY] = lastUpdateId
                            }

                            update.message?.let { message ->
                                val chatId = message.chat.id
                                val messageText = message.text ?: ""

                                // Jika belum ada admin chat ID, set sebagai admin
                                if (getAdminChatId() == null) {
                                    setAdminChatId(chatId)
                                    withContext(Dispatchers.Main) {
                                        onAdminRegistered(chatId)
                                    }
                                    // Kirim welcome message ke admin
                                    api.sendMessage(
                                        chatId,
                                        "✅ Anda terdaftar sebagai Admin!\n\nSekarang Anda akan menerima semua pesan dari user aplikasi U3 dan dapat membalasnya langsung di sini."
                                    )
                                } else if (chatId == getAdminChatId()) {
                                    // Ini balasan dari admin
                                    withContext(Dispatchers.Main) {
                                        onAdminReply(messageText)
                                    }
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("TelegramBot", "Polling error", e)
                    delay(5000)
                }
            }
        }
    }

    fun stopPolling() {
        isPolling = false
    }
}