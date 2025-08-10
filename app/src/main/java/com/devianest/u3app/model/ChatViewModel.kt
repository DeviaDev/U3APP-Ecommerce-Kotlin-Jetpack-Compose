package com.devianest.u3app.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context

class ChatViewModel(private val context: Context) : ViewModel() {
    private val telegramService = TelegramBotService(context)

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    private val _adminRegistered = MutableStateFlow(false)
    val adminRegistered: StateFlow<Boolean> = _adminRegistered

    init {
        startTelegramPolling()
    }

    private fun startTelegramPolling() {
        telegramService.startPolling(
            onAdminReply = { adminMessage ->
                val replyMessage = ChatMessage(
                    message = adminMessage,
                    isUser = false,
                    senderName = "Admin"
                )
                addMessage(replyMessage)
            },
            onAdminRegistered = { chatId ->
                _adminRegistered.value = true
                _isConnected.value = true
            }
        )

        // Cek apakah admin sudah terdaftar
        viewModelScope.launch {
            // Simulasi cek admin (dalam implementasi nyata, cek dari DataStore)
            _isConnected.value = true
        }
    }

    // User mengirim pesan melalui aplikasi
    fun sendUserMessage(message: String, userName: String = "User Aplikasi") {
        // Tambah pesan ke UI dulu
        val userMessage = ChatMessage(
            message = message,
            isUser = true,
            senderName = userName,
            status = MessageStatus.SENDING
        )
        addMessage(userMessage)

        // Kirim ke admin via Telegram
        viewModelScope.launch {
            val success = telegramService.sendUserMessageToAdmin(message, userName)

            // Update status pesan
            updateMessageStatus(userMessage.id, if (success) MessageStatus.DELIVERED else MessageStatus.FAILED)

            if (!success) {
                // Tambahkan pesan error
                val errorMessage = ChatMessage(
                    message = "⚠️ Pesan gagal terkirim ke admin. Pastikan admin sudah chat ke bot terlebih dahulu.",
                    isUser = false,
                    senderName = "System"
                )
                addMessage(errorMessage)
            }
        }
    }

    private fun addMessage(message: ChatMessage) {
        _messages.value = _messages.value + message
    }

    private fun updateMessageStatus(messageId: String, status: MessageStatus) {
        _messages.value = _messages.value.map { message ->
            if (message.id == messageId) {
                message.copy(status = status)
            } else {
                message
            }
        }
    }

    // Fungsi untuk mendapatkan instruksi setup admin
    fun getAdminSetupInstructions(): String {
        return """
        📱 CARA SETUP ADMIN:
        
        1. Buka Telegram
        2. Cari bot: @UnitUsahaUnida_Bot
        3. Kirim pesan apa saja ke bot (contoh: "Halo")
        4. Bot akan mendaftarkan Anda sebagai admin
        5. Setelah itu, semua pesan dari user aplikasi akan masuk ke chat Anda
        6. Balas langsung di Telegram untuk membalas user
        """.trimIndent()
    }

    override fun onCleared() {
        super.onCleared()
        telegramService.stopPolling()
    }
}