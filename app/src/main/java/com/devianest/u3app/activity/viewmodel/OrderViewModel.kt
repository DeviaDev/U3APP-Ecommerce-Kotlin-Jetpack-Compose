package com.devianest.u3app.activity.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

// Data classes untuk Order system
data class SimpleProduct(
    val name: String,
    val price: String,
    val imageRes: Int? = null,
    val description: String = ""
)

data class SimpleOrderItem(
    val product: SimpleProduct,
    val quantity: Int
) {
    val totalPrice: Int
        get() = extractPrice(product.price) * quantity
}

data class Order(
    val id: String,
    val customerName: String,
    val eventName: String,
    val orderDate: String,
    val orderTime: String,
    val paymentMethod: String,
    val items: List<SimpleOrderItem>,
    val totalPrice: Int,
    val status: OrderStatus = OrderStatus.PROSES,
    val createdAt: Long = System.currentTimeMillis()
)

enum class OrderStatus(val displayName: String) {
    PROSES("Proses"),
    SELESAI("Selesai"),
    BATAL("Batal")
}

// Helper function untuk mengekstrak harga dari string
private fun extractPrice(priceString: String): Int {
    return priceString.replace(Regex("[^\\d]"), "").toIntOrNull() ?: 0
}

class OrderViewModel : ViewModel() {
    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    // Counter untuk generate order ID
    private var orderIdCounter = 1

    init {
        // Initialize dengan sample data untuk demo
        initializeSampleData()
    }

    private fun initializeSampleData() {
        val sampleOrders = listOf(
            Order(
                id = "ORD001",
                customerName = "John Doe",
                eventName = "Ulang Tahun",
                orderDate = "15 Agustus 2025",
                orderTime = "14:00 WIB",
                paymentMethod = "Transfer",
                items = listOf(
                    SimpleOrderItem(
                        product = SimpleProduct(
                            name = "Mug UNIDA Premium",
                            price = "Rp 15.000",
                            imageRes = com.devianest.u3app.R.drawable.p1,
                            description = "Mug keramik dengan logo UNIDA"
                        ),
                        quantity = 2
                    )
                ),
                totalPrice = 30000,
                status = OrderStatus.PROSES
            ),
            Order(
                id = "ORD002",
                customerName = "Jane Smith",
                eventName = "Rapat Kantor",
                orderDate = "10 Agustus 2025",
                orderTime = "09:00 WIB",
                paymentMethod = "Cash",
                items = listOf(
                    SimpleOrderItem(
                        product = SimpleProduct(
                            name = "T-Shirt UNIDA",
                            price = "Rp 75.000",
                            imageRes = com.devianest.u3app.R.drawable.p1,
                            description = "Kaos premium UNIDA"
                        ),
                        quantity = 1
                    )
                ),
                totalPrice = 75000,
                status = OrderStatus.SELESAI
            )
        )

        _orders.value = sampleOrders
        orderIdCounter = 3 // Set counter setelah sample data
    }

    fun createOrder(
        customerName: String,
        eventName: String,
        orderDate: String,
        orderTime: String,
        paymentMethod: String,
        cartItems: List<com.devianest.u3app.activity.viewmodel.CartItem>,
        totalPrice: Int
    ) {
        // Generate unique order ID
        val orderId = String.format("ORD%03d", orderIdCounter++)

        // Convert CartItem ke SimpleOrderItem
        val orderItems = cartItems.map { cartItem ->
            SimpleOrderItem(
                product = SimpleProduct(
                    name = cartItem.product.name,
                    price = cartItem.product.price,
                    imageRes = cartItem.product.imageRes,
                    description = cartItem.product.description
                ),
                quantity = cartItem.quantity
            )
        }

        // Buat order baru
        val newOrder = Order(
            id = orderId,
            customerName = customerName,
            eventName = eventName,
            orderDate = orderDate,
            orderTime = orderTime,
            paymentMethod = paymentMethod,
            items = orderItems,
            totalPrice = totalPrice,
            status = OrderStatus.PROSES,
            createdAt = System.currentTimeMillis()
        )

        // Tambahkan ke list orders
        val currentOrders = _orders.value.toMutableList()
        currentOrders.add(0, newOrder) // Add to beginning of list
        _orders.value = currentOrders
    }

    fun updateOrderStatus(orderId: String, newStatus: OrderStatus) {
        val currentOrders = _orders.value.toMutableList()
        val orderIndex = currentOrders.indexOfFirst { it.id == orderId }

        if (orderIndex != -1) {
            currentOrders[orderIndex] = currentOrders[orderIndex].copy(status = newStatus)
            _orders.value = currentOrders
        }
    }

    fun getOrdersByStatus(status: OrderStatus): List<Order> {
        return _orders.value.filter { it.status == status }
    }

    fun getOrderById(orderId: String): Order? {
        return _orders.value.find { it.id == orderId }
    }

    fun getAllOrders(): List<Order> {
        return _orders.value
    }
}