package com.devianest.u3app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devianest.u3app.activity.viewmodel.CartViewModel
import com.devianest.u3app.activity.viewmodel.OrderViewModel
import com.devianest.u3app.activity.screen.*
import com.devianest.u3app.activity.screens.EditProfileScreen
import com.devianest.u3app.activity.screen.AllProductScreen
import com.devianest.u3app.activity.screen.MainScreen
import com.devianest.u3app.uiProject.theme.U3AppTheme
import com.devianest.u3app.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        enableEdgeToEdge()
        setContent {
            U3AppTheme {
                MainAppNavigation()
            }
        }
    }
}

@Composable
fun MainAppNavigation() {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()

    // Get current route to pass to screens
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    // Debug logging
    LaunchedEffect(currentRoute) {
        Log.d("Navigation", "Current route changed to: $currentRoute")
    }

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Home/Main Screen
        composable("home") {
            Log.d("Navigation", "Composing MainScreen")
            MainScreen(
                onSeeMoreClick = {
                    Log.d("Navigation", "Navigating to all_products")
                    navController.navigate("all_products")
                },
                onNavigate = { route ->
                    Log.d("Navigation", "Bottom nav clicked: $route from $currentRoute")
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo("home") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                // ✅ FIX: Tambahkan onCategoryClick yang hilang!
                onCategoryClick = { category ->
                    Log.d("Navigation", "Category clicked: $category")
                    navController.navigate("all_products/$category")
                },
                currentRoute = currentRoute,
                cartViewModel = cartViewModel
            )
        }

        // Cart Screen
        composable("cart") {
            Log.d("Navigation", "Composing CartScreen")
            CartScreen(
                navController = navController,
                currentRoute = currentRoute,
                onNavigate = { route ->
                    Log.d("Navigation", "Cart nav clicked: $route")
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo("home") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                onCheckoutClick = {
                    Log.d("Navigation", "Checkout clicked")
                    navController.navigate("checkout")
                },
                cartViewModel = cartViewModel
            )
        }

        // Orders Screen
        composable("orders") {
            Log.d("Navigation", "Composing OrdersScreen")
            OrdersScreen(
                currentRoute = currentRoute,
                orderViewModel = orderViewModel,
                onNavigate = { route ->
                    Log.d("Navigation", "Orders nav clicked: $route")
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo("home") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        // Message Screen
        composable("message") {
            Log.d("Navigation", "Composing MessageScreen")
            MessageScreen(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    Log.d("Navigation", "Message nav clicked: $route")
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo("home") {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }

        // Profile Screen
        composable("profile") { backStackEntry ->
            val currentRoute = backStackEntry.destination.route ?: ""
            val profileViewModel: ProfileViewModel = viewModel()

            ProfileScreen(
                currentRoute = currentRoute,
                profileViewModel = profileViewModel,
                onNavigate = { route ->
                    when (route) {
                        "edit_profile" -> navController.navigate("edit_profile")
                        else -> {
                            if (route != currentRoute) {
                                navController.navigate(route) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                }
            )
        }

        // Edit Profile Screen
        composable("edit_profile") {
            val profileViewModel: ProfileViewModel = viewModel()
            EditProfileScreen(
                navController = navController,
                profileViewModel = profileViewModel
            )
        }

        // ✅ FIX: All Products Screen TANPA parameter (untuk See More)
        composable("all_products") {
            Log.d("Navigation", "Composing AllProductScreen (no category)")
            AllProductScreen(
                onBack = {
                    Log.d("Navigation", "Back from all_products")
                    navController.popBackStack()
                },
                navController = navController,
                cartViewModel = cartViewModel,
                targetCategory = null // Tidak ada kategori spesifik
            )
        }

        // ✅ FIX: All Products Screen DENGAN parameter category
        composable(
            route = "all_products/{category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")
            Log.d("Navigation", "Composing AllProductScreen with category: $category")

            AllProductScreen(
                onBack = {
                    Log.d("Navigation", "Back from all_products with category")
                    navController.popBackStack()
                },
                navController = navController,
                cartViewModel = cartViewModel,
                targetCategory = category // Pass kategori yang dipilih
            )
        }

        // Checkout Screen
        composable("checkout") {
            Log.d("Navigation", "Composing CheckoutScreen")
            CheckoutScreen(
                cartViewModel = cartViewModel,
                orderViewModel = orderViewModel,
                onBack = {
                    Log.d("Navigation", "Back from checkout")
                    navController.popBackStack()
                },
                onOrderComplete = {
                    Log.d("Navigation", "Order completed, going to orders")
                    navController.navigate("orders") {
                        popUpTo("home") {
                            inclusive = false
                        }
                    }
                }
            )
        }
    }
}