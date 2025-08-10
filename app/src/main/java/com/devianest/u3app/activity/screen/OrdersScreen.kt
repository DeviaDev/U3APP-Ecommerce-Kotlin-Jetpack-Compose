package com.devianest.u3app.activity.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devianest.u3app.activity.component.BottomNavigationBar
import com.devianest.u3app.activity.viewmodel.CartViewModel
import com.devianest.u3app.activity.viewmodel.OrderViewModel
import com.devianest.u3app.activity.viewmodel.Order
import com.devianest.u3app.activity.viewmodel.OrderStatus
import com.devianest.u3app.activity.viewmodel.SimpleOrderItem
import com.devianest.u3app.R
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun OrdersScreen(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    orderViewModel: OrderViewModel, // Tambahkan OrderViewModel parameter
    cartViewModel: CartViewModel? = null
) {
    // Mengambil orders secara real-time dari OrderViewModel
    val orders by orderViewModel.orders.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableStateOf(OrderStatus.PROSES) }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        "Pesanan Saya",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                backgroundColor = Color(0xFF2196F3),
                elevation = 4.dp
            )
        },
        bottomBar = {
            BottomNavigationBar(currentRoute = currentRoute, onNavigate = onNavigate)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Tab untuk filter status
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OrderStatus.values().forEach { status ->
                        TabItem(
                            title = status.displayName,
                            isSelected = selectedTab == status,
                            onClick = { selectedTab = status },
                            icon = when (status) {
                                OrderStatus.PROSES -> Icons.Default.AccessTime
                                OrderStatus.SELESAI -> Icons.Default.CheckCircle
                                OrderStatus.BATAL -> Icons.Default.Cancel
                            },
                            count = orderViewModel.getOrdersByStatus(status).size,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // List pesanan
            val filteredOrders = orderViewModel.getOrdersByStatus(selectedTab)

            if (filteredOrders.isEmpty()) {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.ShoppingBag,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Belum Ada Pesanan ${selectedTab.displayName}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        when (selectedTab) {
                            OrderStatus.PROSES -> "Pesanan yang sedang diproses akan muncul di sini"
                            OrderStatus.SELESAI -> "Pesanan yang sudah selesai akan muncul di sini"
                            OrderStatus.BATAL -> "Pesanan yang dibatalkan akan muncul di sini"
                        },
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredOrders.sortedByDescending { it.createdAt }) { order ->
                        OrderCard(
                            order = order,
                            onStatusChange = { orderId, newStatus ->
                                orderViewModel.updateOrderStatus(orderId, newStatus)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    onStatusChange: ((String, OrderStatus) -> Unit)? = null
) {
    var showStatusDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header dengan status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Order #${order.id}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                StatusChip(
                    status = order.status,
                    onClick = if (onStatusChange != null) {
                        { showStatusDialog = true }
                    } else null
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Customer info
            Text(
                text = "Pemesan: ${order.customerName}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Acara: ${order.eventName}",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = "Tanggal: ${order.orderDate} - ${order.orderTime}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(color = Color(0xFFE0E0E0))

            Spacer(modifier = Modifier.height(12.dp))

            // Items
            order.items.forEach { item ->
                OrderItemRow(item = item)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Divider(color = Color(0xFFE0E0E0))

            Spacer(modifier = Modifier.height(12.dp))

            // Total dan payment method
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Pembayaran: ${order.paymentMethod}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Total: ${formatCurrency(order.totalPrice)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )
                }

                Text(
                    text = formatTimestamp(order.createdAt),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }

    // Dialog untuk mengubah status order (opsional untuk admin)
    if (showStatusDialog && onStatusChange != null) {
        StatusChangeDialog(
            currentStatus = order.status,
            onDismiss = { showStatusDialog = false },
            onStatusChange = { newStatus ->
                onStatusChange(order.id, newStatus)
                showStatusDialog = false
            }
        )
    }
}

@Composable
fun OrderItemRow(item: SimpleOrderItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Gambar produk
        Card(
            modifier = Modifier.size(50.dp),
            elevation = 2.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            item.product.imageRes?.let { imageRes ->
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = item.product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.product.name.take(2).uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Detail produk
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${item.quantity}x @ ${item.product.price}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Harga
        Text(
            text = formatCurrency(item.totalPrice),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun StatusChip(
    status: OrderStatus,
    onClick: (() -> Unit)? = null
) {
    val (backgroundColor, textColor) = when (status) {
        OrderStatus.PROSES -> Color(0xFFFFF3E0) to Color(0xFFE65100)
        OrderStatus.SELESAI -> Color(0xFFE8F5E8) to Color(0xFF2E7D32)
        OrderStatus.BATAL -> Color(0xFFFFEBEE) to Color(0xFFC62828)
    }

    Card(
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp,
        modifier = if (onClick != null) Modifier.clickable { onClick() } else Modifier
    ) {
        Text(
            text = status.displayName,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: Int = 0,
    modifier: Modifier = Modifier
) {
    Card(
        backgroundColor = if (isSelected) Color(0xFF2196F3) else Color.White,
        elevation = if (isSelected) 6.dp else 2.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .clickable { onClick() }
            .padding(horizontal = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) Color.White else Color.Gray
                )
            }

            if (count > 0) {
                Text(
                    text = "($count)",
                    fontSize = 10.sp,
                    color = if (isSelected) Color.White else Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun StatusChangeDialog(
    currentStatus: OrderStatus,
    onDismiss: () -> Unit,
    onStatusChange: (OrderStatus) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Ubah Status Pesanan")
        },
        text = {
            Column {
                Text("Pilih status baru untuk pesanan ini:")
                Spacer(modifier = Modifier.height(16.dp))

                OrderStatus.values().forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onStatusChange(status) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = status == currentStatus,
                            onClick = { onStatusChange(status) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(status.displayName)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Tutup")
            }
        }
    )
}

// Helper functions
private fun formatCurrency(amount: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount).replace("IDR", "Rp")
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale("id", "ID"))
    return sdf.format(Date(timestamp))
}