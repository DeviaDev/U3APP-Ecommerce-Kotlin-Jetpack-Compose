package com.devianest.u3app.utils

import androidx.navigation.NavHostController

fun NavHostController.navigateSingleTopTo(route: String) {
    this.navigate(route) {
        popUpTo(this@navigateSingleTopTo.graph.startDestinationRoute ?: "main") {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
