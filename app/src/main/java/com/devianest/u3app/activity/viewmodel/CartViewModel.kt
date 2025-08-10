package com.devianest.u3app.activity.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.devianest.u3app.activity.component.Product

data class CartItem(
    val product: Product,
    val quantity: Int = 1
) {
    val totalPrice: Int
        get() = (product.price
            .replace("Rp", "")
            .replace(".", "")
            .replace(",", "")
            .toIntOrNull() ?: 0) * quantity
}

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    fun addToCart(product: Product) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.product.name == product.name }

        if (existingItemIndex >= 0) {
            // Product already exists, increase quantity
            currentItems[existingItemIndex] = currentItems[existingItemIndex].copy(
                quantity = currentItems[existingItemIndex].quantity + 1
            )
        } else {
            // New product, add to cart
            currentItems.add(CartItem(product, 1))
        }

        _cartItems.value = currentItems
    }

    fun removeFromCart(product: Product) {
        val currentItems = _cartItems.value.toMutableList()
        currentItems.removeAll { it.product.name == product.name }
        _cartItems.value = currentItems
    }

    fun updateQuantity(product: Product, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeFromCart(product)
            return
        }

        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.product.name == product.name }

        if (existingItemIndex >= 0) {
            currentItems[existingItemIndex] = currentItems[existingItemIndex].copy(
                quantity = newQuantity
            )
            _cartItems.value = currentItems
        }
    }

    fun increaseQuantity(product: Product) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.product.name == product.name }

        if (existingItemIndex >= 0) {
            currentItems[existingItemIndex] = currentItems[existingItemIndex].copy(
                quantity = currentItems[existingItemIndex].quantity + 1
            )
            _cartItems.value = currentItems
        }
    }

    fun decreaseQuantity(product: Product) {
        val currentItems = _cartItems.value.toMutableList()
        val existingItemIndex = currentItems.indexOfFirst { it.product.name == product.name }

        if (existingItemIndex >= 0) {
            val currentQuantity = currentItems[existingItemIndex].quantity
            if (currentQuantity > 1) {
                currentItems[existingItemIndex] = currentItems[existingItemIndex].copy(
                    quantity = currentQuantity - 1
                )
                _cartItems.value = currentItems
            } else {
                removeFromCart(product)
            }
        }
    }

    fun getTotalItems(): Int = _cartItems.value.sumOf { it.quantity }

    fun getTotalPrice(): Int = _cartItems.value.sumOf { it.totalPrice }

    fun clearCart() {
        _cartItems.value = emptyList()
    }
}