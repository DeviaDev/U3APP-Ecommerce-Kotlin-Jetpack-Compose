package com.devianest.u3app.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val NIM = stringPreferencesKey("nim")
        val PRODI = stringPreferencesKey("prodi")
        val SEMESTER = stringPreferencesKey("semester")
        val STATUS = stringPreferencesKey("status")
    }

    // Simpan data
    suspend fun saveProfile(name: String, email: String, nim: String, prodi: String, semester: String, status: String) {
        context.dataStore.edit { prefs ->
            prefs[NAME] = name
            prefs[EMAIL] = email
            prefs[NIM] = nim
            prefs[PRODI] = prodi
            prefs[SEMESTER] = semester
            prefs[STATUS] = status
        }
    }

    // Ambil data
    val profile: Flow<ProfileData> = context.dataStore.data.map { prefs ->
        ProfileData(
            name = prefs[NAME] ?: "",
            email = prefs[EMAIL] ?: "",
            nim = prefs[NIM] ?: "",
            prodi = prefs[PRODI] ?: "",
            semester = prefs[SEMESTER] ?: "",
            status = prefs[STATUS] ?: ""
        )
    }
}

// Data class untuk menyimpan profil
data class ProfileData(
    val name: String,
    val email: String,
    val nim: String,
    val prodi: String,
    val semester: String,
    val status: String
)
