package com.devianest.u3app.activity.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devianest.u3app.activity.viewmodel.CartItem
import com.devianest.u3app.activity.viewmodel.CartViewModel
import com.devianest.u3app.activity.viewmodel.OrderViewModel
import com.devianest.u3app.activity.component.Product
import com.devianest.u3app.R
import java.text.NumberFormat
import java.util.*

@Composable
fun CheckoutScreen(
    cartViewModel: CartViewModel,
    orderViewModel: OrderViewModel, // Tambahkan OrderViewModel
    onBack: () -> Unit,
    onOrderComplete: () -> Unit
) {
    // Mengambil data cart secara real-time dari ViewModel
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    val totalPrice = cartViewModel.getTotalPrice()
    val totalItems = cartViewModel.getTotalItems()

    var selectedMethod by remember { mutableStateOf<String?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var createdOrderId by remember { mutableStateOf("") }

    // Data pemesan yang wajib diisi (awalnya kosong)
    var customerName by remember { mutableStateOf("") }
    var eventName by remember { mutableStateOf("") }
    var orderDate by remember { mutableStateOf("") }
    var orderTime by remember { mutableStateOf("") }

    // Validasi apakah detail pemesan sudah lengkap
    val isCustomerDataComplete = customerName.isNotBlank() &&
            eventName.isNotBlank() &&
            orderDate.isNotBlank() &&
            orderTime.isNotBlank()

    val scrollState = rememberScrollState()

    // Show edit dialog if customer data is not complete on first load
    LaunchedEffect(Unit) {
        if (!isCustomerDataComplete) {
            showEditDialog = true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            modifier = Modifier.statusBarsPadding(),
            title = {
                Text(
                    "CHECKOUT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Kembali",tint = Color.White)
                }
            },
            backgroundColor = Color(0xFF2191DF),
            contentColor = Color.White,
            elevation = 4.dp
        )

        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Section 1: Detail Pemesan
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Detail Pemesan",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF2E7D32)
                            )
                            if (!isCustomerDataComplete) {
                                Icon(
                                    Icons.Default.Warning,
                                    contentDescription = "Wajib diisi",
                                    tint = Color.Red,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .size(20.dp)
                                )
                            }
                        }
                        IconButton(
                            onClick = { showEditDialog = true },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color(0xFF2E7D32)
                            )
                        }
                    }

                    if (!isCustomerDataComplete) {
                        Text(
                            "* Detail pemesan wajib diisi",
                            fontSize = 12.sp,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Divider(
                        color = Color(0xFFE0E0E0),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    if (isCustomerDataComplete) {
                        InfoRow("Nama Pemesan", customerName)
                        InfoRow("Acara", eventName)
                        InfoRow("Tanggal Pengambilan", orderDate)
                        InfoRow("Waktu Pengambilan", orderTime)
                    } else {
                        Text(
                            "Klik tombol edit untuk mengisi detail pemesan",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            style = androidx.compose.ui.text.TextStyle(
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        )
                    }
                }
            }

            // Section 2: Preview Pesanan (Real-time dari Cart)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Preview Pesanan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF2E7D32)
                    )

                    Divider(
                        color = Color(0xFFE0E0E0),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Items pesanan dari cart real-time
                    if (cartItems.isNotEmpty()) {
                        cartItems.forEachIndexed { index, item ->
                            CartItemRowCheckout(item = item)

                            if (index < cartItems.size - 1) {
                                Divider(
                                    color = Color(0xFFF0F0F0),
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }

                        Divider(
                            color = Color(0xFFE0E0E0),
                            thickness = 2.dp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        // Total
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Total Pesanan ($totalItems Produk)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                formatCurrency(totalPrice),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    } else {
                        // Empty cart state
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Keranjang belanja kosong",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "Tambahkan produk ke keranjang terlebih dahulu",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Section 3: Metode Pembayaran
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Metode Pembayaran",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFF2E7D32)
                    )

                    if (selectedMethod == null && isCustomerDataComplete && cartItems.isNotEmpty()) {
                        Text(
                            "Pilih metode pembayaran terlebih dahulu",
                            fontSize = 14.sp,
                            color = Color.Red,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Payment Method Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PaymentMethodButton(
                            text = "Cash",
                            isSelected = selectedMethod == "Cash",
                            onClick = {
                                if (isCustomerDataComplete && cartItems.isNotEmpty()) {
                                    selectedMethod = "Cash"
                                }
                            },
                            enabled = isCustomerDataComplete && cartItems.isNotEmpty(),
                            modifier = Modifier.weight(1f)
                        )
                        PaymentMethodButton(
                            text = "Transfer",
                            isSelected = selectedMethod == "Transfer",
                            onClick = {
                                if (isCustomerDataComplete && cartItems.isNotEmpty()) {
                                    selectedMethod = "Transfer"
                                }
                            },
                            enabled = isCustomerDataComplete && cartItems.isNotEmpty(),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Payment Notes
                    selectedMethod?.let { method ->
                        Spacer(modifier = Modifier.height(12.dp))

                        val note = when (method) {
                            "Cash" -> "💰 Pembayaran secara cash dibayarkan ketika pengambilan pesanan dengan menunjukkan invoice pesanan"
                            "Transfer" -> "🏦 Pembayaran secara Transfer:\n" +
                                    "• Nomor Rekening: 9837362792 (Bank BJB)\n" +
                                    "• Atas Nama: Unit Usaha Unida Mantingan\n" +
                                    "• Setelah transfer, kirim bukti transfer ke WhatsApp: 081234567890\n" +
                                    "• Pengambilan pesanan dengan menunjukkan bukti transfer"
                            else -> ""
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = if (method == "Cash") Color(0xFFFFF3E0) else Color(0xFFE8F5E8),
                            elevation = 2.dp,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = note,
                                fontSize = 14.sp,
                                color = if (method == "Cash") Color(0xFFE65100) else Color(0xFF2E7D32),
                                modifier = Modifier.padding(12.dp),
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }

            // Section 4: Buat Pesanan Button
            val canCreateOrder = isCustomerDataComplete && selectedMethod != null && cartItems.isNotEmpty()

            Button(
                onClick = {
                    if (canCreateOrder) {
                        // Buat order baru menggunakan OrderViewModel
                        orderViewModel.createOrder(
                            customerName = customerName,
                            eventName = eventName,
                            orderDate = orderDate,
                            orderTime = orderTime,
                            paymentMethod = selectedMethod!!,
                            cartItems = cartItems,
                            totalPrice = totalPrice
                        )

                        // Get the latest order ID for success message
                        val latestOrders = orderViewModel.getAllOrders()
                        createdOrderId = if (latestOrders.isNotEmpty()) {
                            latestOrders.sortedByDescending { it.createdAt }.first().id
                        } else {
                            "ORD001"
                        }

                        // Show success dialog
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = canCreateOrder,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (canCreateOrder) Color(0xFF2E7D32) else Color.Gray,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = if (canCreateOrder) 4.dp else 0.dp
                )
            ) {
                Text(
                    text = when {
                        cartItems.isEmpty() -> "Keranjang Kosong"
                        !isCustomerDataComplete -> "Lengkapi Detail Pemesan"
                        selectedMethod == null -> "Pilih Metode Pembayaran"
                        else -> "Buat Pesanan"
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Dialog Edit Pemesan
    if (showEditDialog) {
        EditCustomerDialog(
            customerName = customerName,
            eventName = eventName,
            orderDate = orderDate,
            orderTime = orderTime,
            onDismiss = {
                if (isCustomerDataComplete) {
                    showEditDialog = false
                }
            },
            onSave = { name, event, date, time ->
                customerName = name
                eventName = event
                orderDate = date
                orderTime = time
                showEditDialog = false
            },
            isRequired = !isCustomerDataComplete
        )
    }

    // Dialog Success Order
    if (showSuccessDialog) {
        OrderSuccessDialog(
            orderId = createdOrderId,
            onDismiss = {
                showSuccessDialog = false
                // Clear cart setelah dialog ditutup
                cartViewModel.clearCart()
                onOrderComplete()
            }
        )
    }
}

@Composable
fun OrderSuccessDialog(
    orderId: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Icon
                Card(
                    modifier = Modifier.size(80.dp),
                    backgroundColor = Color(0xFFE8F5E8),
                    shape = androidx.compose.foundation.shape.CircleShape,
                    elevation = 0.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "✓",
                            fontSize = 40.sp,
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Pesanan Berhasil Dibuat!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Order ID: $orderId",
                    fontSize = 16.sp,
                    color = Color(0xFF2E7D32),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "Pesanan Anda telah berhasil dibuat. Anda dapat memantau status pesanan di halaman Pesanan Saya.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF2E7D32)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "Lihat Pesanan Saya",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemRowCheckout(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Gambar produk
        Card(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(8.dp)),
            elevation = 2.dp
        ) {
            // Jika ada imageRes, gunakan Image, jika tidak gunakan placeholder
            item.product.imageRes?.let { imageRes ->
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = item.product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                // Placeholder jika tidak ada gambar
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.product.name.take(2).uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
        }

        // Detail produk
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.product.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                maxLines = 2
            )
            Text(
                text = "${item.quantity}x @ ${item.product.price}",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Harga total item
        Text(
            text = formatCurrency(item.totalPrice),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color(0xFF2E7D32)
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun PaymentMethodButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(enabled = enabled) { onClick() }
            .height(50.dp),
        backgroundColor = when {
            !enabled -> Color(0xFFF5F5F5)
            isSelected -> Color(0xFF2E7D32)
            else -> Color.White
        },
        elevation = if (isSelected && enabled) 6.dp else 2.dp,
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(
            1.dp,
            if (enabled) Color(0xFFE0E0E0) else Color(0xFFF0F0F0)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = when {
                    !enabled -> Color.Gray
                    isSelected -> Color.White
                    else -> Color.Black
                },
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun EditCustomerDialog(
    customerName: String,
    eventName: String,
    orderDate: String,
    orderTime: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit,
    isRequired: Boolean = false
) {
    var tempName by remember { mutableStateOf(customerName) }
    var tempEvent by remember { mutableStateOf(eventName) }
    var tempDate by remember { mutableStateOf(orderDate) }
    var tempTime by remember { mutableStateOf(orderTime) }

    val isValid = tempName.isNotBlank() &&
            tempEvent.isNotBlank() &&
            tempDate.isNotBlank() &&
            tempTime.isNotBlank()

    Dialog(onDismissRequest = { if (!isRequired) onDismiss() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Detail Pemesan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.weight(1f)
                    )
                    if (isRequired) {
                        Text(
                            "* Wajib diisi",
                            fontSize = 12.sp,
                            color = Color.Red
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = tempName,
                    onValueChange = { tempName = it },
                    label = { Text("Nama Pemesan *") },
                    isError = tempName.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = tempEvent,
                    onValueChange = { tempEvent = it },
                    label = { Text("Acara *") },
                    isError = tempEvent.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = tempDate,
                    onValueChange = { tempDate = it },
                    label = { Text("Tanggal Pengambilan/Penjemputan *") },
                    placeholder = { Text("Contoh: 21 Juni 2025") },
                    isError = tempDate.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = tempTime,
                    onValueChange = { tempTime   = it },
                    label = { Text("Waktu Pengambilan/Penjemputan *") },
                    placeholder = { Text("Contoh: 10:00 WIB") },
                    isError = tempTime.isBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )

                if (!isValid) {
                    Text(
                        "Semua field wajib diisi",
                        fontSize = 12.sp,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!isRequired) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Batal")
                        }
                    }

                    Button(
                        onClick = {
                            if (isValid) {
                                onSave(tempName, tempEvent, tempDate, tempTime)
                            }
                        },
                        enabled = isValid,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF2E7D32),
                            disabledBackgroundColor = Color.Gray
                        )
                    ) {
                        Text("Simpan", color = Color.White)
                    }
                }
            }
        }
    }
}

// Helper function to format currency for checkout
private fun formatCurrency(amount: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount).replace("IDR", "Rp")
}

@Preview(showBackground = true)
@Composable
fun CheckoutScreenPreview() {
    // Sample CartViewModel dan OrderViewModel untuk preview
    val sampleCartViewModel = remember { CartViewModel() }.apply {
        addToCart(
            Product(
                name = "Mug UNIDA Premium",
                price = "Rp 15.000",
                imageRes = R.drawable.p1,
                description = "Mug keramik dengan logo UNIDA",
                rating = 4.6
            )
        )
        addToCart(
            Product(
                name = "T-Shirt UNIDA",
                price = "Rp 75.000",
                imageRes = R.drawable.p1,
                description = "Kaos premium UNIDA",
                rating = 4.8
            )
        )
    }

    val sampleOrderViewModel = remember { OrderViewModel() }

    CheckoutScreen(
        cartViewModel = sampleCartViewModel,
        orderViewModel = sampleOrderViewModel,
        onBack = {},
        onOrderComplete = {}
    )
}