package com.devianest.u3app.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

data class User(
    val username: String = "",
    val email: String = "",
    val password: String = ""
) {
    override fun toString(): String {
        return "Username: $username, Email: $email"
    }
}

class AuthViewModel : ViewModel() {
    // Daftar user terdaftar (in-memory)
    private val _registeredUsers = mutableStateListOf<User>()

    // Current user yang sedang login
    var currentUser by mutableStateOf<User?>(null)
        private set

    // State untuk loading dan error
    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf("")
        private set

    init {
        // Data dummy untuk testing
        _registeredUsers.add(User("admin", "admin@test.com", "123456"))
        _registeredUsers.add(User("user1", "user1@test.com", "password"))
    }

    // Fungsi register
    suspend fun register(
        username: String,
        email: String,
        password: String,
        repeatPassword: String
    ): Boolean {
        errorMessage = ""
        isLoading = true

        delay(1000) // Simulasi network delay

        try {
            // Validasi
            when {
                username.isBlank() -> {
                    errorMessage = "Username tidak boleh kosong"
                    return false
                }
                email.isBlank() -> {
                    errorMessage = "Email tidak boleh kosong"
                    return false
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    errorMessage = "Format email tidak valid"
                    return false
                }
                password.isBlank() -> {
                    errorMessage = "Password tidak boleh kosong"
                    return false
                }
                password.length < 6 -> {
                    errorMessage = "Password minimal 6 karakter"
                    return false
                }
                password != repeatPassword -> {
                    errorMessage = "Password dan konfirmasi password tidak sama"
                    return false
                }
                _registeredUsers.any { it.email == email } -> {
                    errorMessage = "Email sudah terdaftar"
                    return false
                }
                _registeredUsers.any { it.username == username } -> {
                    errorMessage = "Username sudah digunakan"
                    return false
                }
            }

            // Simpan user baru dan langsung login
            val newUser = User(username, email, password)
            _registeredUsers.add(newUser)
            currentUser = newUser

            return true
        } finally {
            isLoading = false
        }
    }

    // Fungsi login
    suspend fun login(email: String, password: String): Boolean {
        errorMessage = ""
        isLoading = true

        delay(1000) // Simulasi network delay

        try {
            when {
                email.isBlank() -> {
                    errorMessage = "Email tidak boleh kosong"
                    return false
                }
                password.isBlank() -> {
                    errorMessage = "Password tidak boleh kosong"
                    return false
                }
            }

            // Cari user
            val user = _registeredUsers.find {
                it.email == email && it.password == password
            }

            if (user != null) {
                currentUser = user
                return true
            } else {
                errorMessage = "Email atau password salah"
                return false
            }
        } finally {
            isLoading = false
        }
    }

    // Fungsi logout
    fun logout() {
        currentUser = null
        errorMessage = ""
    }

    // Clear error message
    fun clearError() {
        errorMessage = ""
    }
}