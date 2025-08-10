package com.devianest.u3app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.devianest.u3app.activity.screen.*
import com.devianest.u3app.activity.screen.AllProductScreen
import com.devianest.u3app.activity.screens.EditProfileScreen
import com.devianest.u3app.activity.viewmodel.CartViewModel
import com.devianest.u3app.activity.viewmodel.OrderViewModel
import com.devianest.u3app.activity.screen.MainScreen
import com.devianest.u3app.viewmodel.ProfileViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    cartViewModel: CartViewModel,
    profileViewModel: ProfileViewModel,
    orderViewModel: OrderViewModel
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: "main"

    NavHost(navController = navController, startDestination = "main") {

        // ✅ Home
        composable("main") {
            MainScreen(
                cartViewModel = cartViewModel,
                onSeeMoreClick = { navController.navigate("all_products") },
                onNavigate = { navController.navigateSingleTopTo(it) },
                currentRoute = currentRoute
            )
        }

        // ✅ All Product
        composable("all_products") {
            AllProductScreen(
                cartViewModel = cartViewModel,
                onBack = { navController.popBackStack() },
                navController = navController,
            )
        }

        // ✅ Cart
        composable("CartScreen") {
            CartScreen(
                cartViewModel = cartViewModel,
                navController = navController,
                currentRoute = currentRoute,
                onNavigate = { navController.navigateSingleTopTo(it) },
                onCheckoutClick = { navController.navigate("checkout") }
            )
        }

        // ✅ Checkout
        composable("checkout") {
            CheckoutScreen(
                cartViewModel = cartViewModel,
                orderViewModel = orderViewModel,
                onBack = { navController.popBackStack() },
                onOrderComplete = {
                    // Clear cart setelah order selesai
                    cartViewModel.clearCart()
                    // Navigate to OrderScreen
                    navController.navigate("OrderScreen") {
                        popUpTo("main") {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        // ✅ Order
        composable("OrderScreen") {
            OrdersScreen(
                orderViewModel = orderViewModel,
                currentRoute = currentRoute,
                onNavigate = { navController.navigateSingleTopTo(it) },
                cartViewModel = cartViewModel
            )
        }

        // ✅ Message
        composable("MessageScreen") {
            MessageScreen(
                currentRoute = currentRoute,
                onNavigate = { navController.navigateSingleTopTo(it) }
            )
        }

        // ✅ Profile - FIXED
        composable("ProfileScreen") {
            // Option 1: Gunakan parameter yang sudah ada (RECOMMENDED)
            ProfileScreen(
                currentRoute = currentRoute,
                profileViewModel = profileViewModel, // Gunakan parameter yang sudah diteruskan
                onNavigate = {
                    when (it) {
                        "edit_profile" -> navController.navigate("edit_profile")
                        else -> navController.navigateSingleTopTo(it)
                    }
                }
            )

            // Option 2: Jika tetap ingin menggunakan hiltViewModel (alternative)
            /*
            val hiltProfileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                currentRoute = currentRoute,
                profileViewModel = hiltProfileViewModel,
                onNavigate = {
                    when (it) {
                        "edit_profile" -> navController.navigate("edit_profile")
                        else -> navController.navigateSingleTopTo(it)
                    }
                }
            )
            */
        }

        // ✅ Edit Profile - FIXED
        composable("edit_profile") {
            // Option 1: Gunakan parameter yang sudah ada (RECOMMENDED)
            EditProfileScreen(
                navController = navController,
                profileViewModel = profileViewModel // Gunakan parameter yang sudah diteruskan
            )

            // Option 2: Jika tetap ingin menggunakan hiltViewModel (alternative)
            /*
            val hiltProfileViewModel: ProfileViewModel = hiltViewModel()
            EditProfileScreen(
                navController = navController,
                profileViewModel = hiltProfileViewModel
            )
            */
        }
    }
}

// ✅ Extension untuk navigasi
fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.startDestinationRoute ?: "main") {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}