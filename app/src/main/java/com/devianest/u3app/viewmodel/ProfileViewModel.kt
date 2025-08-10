// ProfileViewModel.kt - ViewModel untuk mengelola state profile
package com.devianest.u3app.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devianest.u3app.data.ProfileData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _profileData = MutableStateFlow(ProfileData())
    val profileData: StateFlow<ProfileData> = _profileData.asStateFlow()

    fun updateProfile(newProfileData: ProfileData) {
        viewModelScope.launch {
            _profileData.value = newProfileData
        }
    }

    fun updateProfileImage(imageUri: Uri?) {
        viewModelScope.launch {
            _profileData.value = _profileData.value.copy(profileImage = imageUri)
        }
    }
}