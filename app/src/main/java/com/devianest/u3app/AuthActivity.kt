package com.devianest.u3app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.devianest.u3app.activity.screen.LoginScreen
import com.devianest.u3app.activity.screen.RegisterScreen
import com.devianest.u3app.uiProject.theme.U3AppTheme
import com.devianest.u3app.viewmodel.AuthViewModel

class AuthActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cek dulu apakah user sudah login
        if (authViewModel.currentUser != null) {
            // User sudah login, langsung ke MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return // supaya kode di bawah tidak dieksekusi
        }

        val startWithLogin = intent.getBooleanExtra("start_with_login", true)


        // Di AuthActivity onCreate, ganti jadi:
        setContent {
            U3AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthNavigation(
                        authViewModel = authViewModel,
                        startWithLogin = startWithLogin,
                        onAuthSuccess = {
                            println("🔥 AUTH SUCCESS! Going to MainActivity...")

                            // ✅ PASTIKAN USER TERSIMPAN
                            if (authViewModel.currentUser != null) {
                                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                println("🚨 ERROR: User null after login!")
                            }
                        }
                    )
                }
            }
        }


    }
}

@Composable
fun AuthNavigation(
    authViewModel: AuthViewModel,
    startWithLogin: Boolean,
    onAuthSuccess: () -> Unit
) {
    val navController = rememberNavController()
    val startDestination = if (startWithLogin) "login" else "register"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = onAuthSuccess,
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        composable("register") {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = onAuthSuccess,
                onNavigateToLogin = {
                    navController.navigate("login")
                },
                onBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.popBackStack()
                    } else {
                        navController.navigate("login")
                    }
                }
            )
        }
    }
}