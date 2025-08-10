package com.devianest.u3app.activity.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devianest.u3app.R
import com.devianest.u3app.viewmodel.ProfileViewModel
import com.devianest.u3app.viewmodel.AuthViewModel
import com.devianest.u3app.activity.component.BottomNavigationBar

@Composable
fun ProfileScreen(
    currentRoute: String,
    profileViewModel: ProfileViewModel,
    authViewModel: AuthViewModel? = null,
    onNavigate: (String) -> Unit
) {
    val profileData by profileViewModel.profileData.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header dengan informasi user
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colorResource(R.color.old).copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Image
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(colorResource(R.color.old).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Perbaikan: safe handling untuk semua tipe data
                        val profileImage = profileData.profileImage?.toString() ?: ""
                        if (profileImage.isNotEmpty()) {
                            Image(
                                painter = painterResource(R.drawable.splash_pic), // Placeholder
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Profile",
                                modifier = Modifier.size(40.dp),
                                tint = colorResource(R.color.old)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // User Info
                    // Perbaikan: safe handling untuk semua tipe data
                    val userName = profileData.nama?.toString() ?: ""
                    Text(
                        text = if (userName.isNotEmpty()) userName else "User",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(R.color.black)
                    )

                    // Perbaikan: safe handling untuk semua tipe data
                    val userEmail = profileData.email?.toString() ?: ""
                    Text(
                        text = if (userEmail.isNotEmpty()) userEmail else "user@email.com",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    // Edit Profile Button
                    OutlinedButton(
                        onClick = { onNavigate("edit_profile") },
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = colorResource(R.color.old)
                        ),
                        border = BorderStroke(1.dp, colorResource(R.color.old)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit Profile")
                    }
                }
            }

            // Menu Items
            Text(
                text = "Account Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Account Settings Items
            ProfileMenuItem(
                icon = Icons.Default.Person,
                title = "Personal Information",
                subtitle = "Update your personal details",
                onClick = { onNavigate("edit_profile") }
            )

            ProfileMenuItem(
                icon = Icons.Default.ShoppingCart,
                title = "My Orders",
                subtitle = "View your order history",
                onClick = { onNavigate("orders") }
            )

            ProfileMenuItem(
                icon = Icons.Default.Favorite,
                title = "Wishlist",
                subtitle = "View your saved items",
                onClick = { /* Handle wishlist */ }
            )

            ProfileMenuItem(
                icon = Icons.Default.LocationOn,
                title = "Addresses",
                subtitle = "Manage your delivery addresses",
                onClick = { /* Handle addresses */ }
            )

            ProfileMenuItem(
                icon = Icons.Default.CreditCard,
                title = "Payment Methods",
                subtitle = "Manage your payment options",
                onClick = { /* Handle payment methods */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // App Settings
            Text(
                text = "App Settings",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.black),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ProfileMenuItem(
                icon = Icons.Default.Notifications,
                title = "Notifications",
                subtitle = "Manage your notification preferences",
                onClick = { /* Handle notifications */ }
            )

            ProfileMenuItem(
                icon = Icons.Default.Security,
                title = "Privacy & Security",
                subtitle = "Manage your privacy settings",
                onClick = { /* Handle privacy */ }
            )

            ProfileMenuItem(
                icon = Icons.Default.Help,
                title = "Help & Support",
                subtitle = "Get help and contact support",
                onClick = { /* Handle help */ }
            )

            ProfileMenuItem(
                icon = Icons.Default.Info,
                title = "About",
                subtitle = "About U3App",
                onClick = { /* Handle about */ }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Logout Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigate("logout") },
                colors = CardDefaults.cardColors(
                    containerColor = Color.Red.copy(alpha = 0.1f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Logout",
                        tint = Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Red
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Arrow",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Bottom Navigation - Menggunakan komponen yang sudah ada
        BottomNavigationBar(
            currentRoute = currentRoute,
            onNavigate = onNavigate
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        colorResource(R.color.old).copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = colorResource(R.color.old),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.black)
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Arrow",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}