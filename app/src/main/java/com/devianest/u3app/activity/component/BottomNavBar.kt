package com.devianest.u3app.activity.component

import android.util.Log
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    // PENTING: Ganti "main" dengan "home"
    val items = listOf(
        BottomNavItem("orders", Icons.Default.Receipt, "Order"),
        BottomNavItem("cart", Icons.Default.ShoppingCart, "Cart"),
        BottomNavItem("home", Icons.Default.Home, "Home"), // ✅ Ubah dari "main" ke "home"
        BottomNavItem("message", Icons.Default.Chat, "Message"),
        BottomNavItem("profile", Icons.Default.Person, "Profile")
    )

    Log.d("BottomNav", "Current route: $currentRoute")

    BottomNavigation(
        backgroundColor = Color(0xFF2191DF), // warna custom
        contentColor = Color.White,
        elevation = 8.dp,
        modifier = Modifier.navigationBarsPadding()
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            Log.d("BottomNav", "Item: ${item.route}, Selected: $isSelected")

            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    Log.d("BottomNav", "Clicked: ${item.route}, Current: $currentRoute")
                    if (currentRoute != item.route) {
                        onNavigate(item.route)
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) },
                alwaysShowLabel = true,
                selectedContentColor = Color.White, // warna icon & label saat dipilih
                unselectedContentColor = MaterialTheme.colors.onSurface.copy(alpha = 0.6f) // warna saat tidak dipilih
            )
        }
    }
}