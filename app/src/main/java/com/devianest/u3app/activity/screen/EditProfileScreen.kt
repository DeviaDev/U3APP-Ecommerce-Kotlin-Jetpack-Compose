package com.devianest.u3app.activity.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devianest.u3app.viewmodel.ProfileViewModel
import com.devianest.u3app.viewmodel.AuthViewModel
import com.devianest.u3app.data.ProfileData
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    authViewModel: AuthViewModel? = null
) {
    // Collect ProfileData state
    val profileDataState = profileViewModel.profileData.collectAsState()
    val profileData = profileDataState.value

    // State variables
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var showSuccessMessage by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Initialize form dengan data dari profileData
    LaunchedEffect(profileData) {
        name = profileData.nama
        email = profileData.email
        phone = profileData.phone
    }

    // Save function
    fun handleSave() {
        coroutineScope.launch {
            try {
                profileViewModel.updateProfile(
                    ProfileData(
                        nama = name,
                        email = email,
                        nim = profileData.nim,
                        prodi = profileData.prodi,
                        semester = profileData.semester,
                        profileImage = profileData.profileImage,
                        phone = phone
                    )
                )
                showSuccessMessage = true
                delay(1500) // Show success message briefly
                navController.popBackStack()
            } catch (e: Exception) {
                // Handle error jika diperlukan
            }
        }
    }

    // UI Content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
            Text(
                text = "Edit Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = { handleSave() }) {
                Text(
                    text = "Save",
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Profile Image Section
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(60.dp),
                tint = Color.Gray
            )

            // Edit button overlay
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Blue)
                    .align(Alignment.BottomEnd)
                    .clickable { /* Handle image change */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Image",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Form Fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nama") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Name"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email"
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("No. Telepon") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone"
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = true
        )

        // Non-editable fields (read-only)
        OutlinedTextField(
            value = profileData.nim,
            onValueChange = { /* Read only */ },
            label = { Text("NIM") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Badge,
                    contentDescription = "NIM"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = false,
            singleLine = true
        )

        OutlinedTextField(
            value = profileData.prodi,
            onValueChange = { /* Read only */ },
            label = { Text("Program Studi") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Prodi"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = false,
            singleLine = true
        )

        OutlinedTextField(
            value = profileData.semester,
            onValueChange = { /* Read only */ },
            label = { Text("Semester") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Semester"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            enabled = false,
            singleLine = true
        )

        // Save Button
        Button(
            onClick = { handleSave() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue
            )
        ) {
            Text(
                text = "Simpan Perubahan",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }

    // Success message
    if (showSuccessMessage) {
        LaunchedEffect(showSuccessMessage) {
            delay(2000)
            showSuccessMessage = false
        }

        // Optional: Show a snackbar or toast
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.Green.copy(alpha = 0.9f)
                )
            ) {
                Text(
                    text = "Profile berhasil diperbarui!",
                    color = Color.White,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}