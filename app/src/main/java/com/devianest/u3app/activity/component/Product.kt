package com.devianest.u3app.activity.component

data class Product(
    val name: String,
    val price: String,
    val imageUrl: String? = null,
    val description: String,
    val rating: Double,
    val imageRes: Int
)

