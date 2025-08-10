package com.devianest.u3app.activity.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devianest.u3app.R
import com.devianest.u3app.activity.component.BottomNavigationBar
import com.devianest.u3app.activity.component.Product
import com.devianest.u3app.activity.viewmodel.CartViewModel
import com.devianest.u3app.activity.viewmodel.CartItem
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.text.NumberFormat
import java.util.*

@Composable
fun CartScreen(
    navController: NavController,
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onCheckoutClick: () -> Unit,
    cartViewModel: CartViewModel
) {
    val cartItems by cartViewModel.cartItems.collectAsStateWithLifecycle()
    val totalItems = cartViewModel.getTotalItems()
    val totalPrice = cartViewModel.getTotalPrice()

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        "Keranjang Belanja",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                backgroundColor = Color(0xFF2196F3),
                elevation = 4.dp
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (cartItems.isEmpty()) {
                // Empty cart state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.ShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Keranjang Belanja Kosong",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Yuk, isi keranjangmu dengan produk-produk menarik!",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { onNavigate("home") },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2196F3)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Mulai Belanja", color = Color.White)
                    }
                }
            } else {
                // Cart with items
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(cartItems) { cartItem ->
                        CartItemCard(
                            cartItem = cartItem,
                            onIncreaseQuantity = { cartViewModel.increaseQuantity(cartItem.product) },
                            onDecreaseQuantity = { cartViewModel.decreaseQuantity(cartItem.product) },
                            onRemoveClick = { cartViewModel.removeFromCart(cartItem.product) }
                        )
                    }
                }

                // Bottom summary and checkout
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Total ($totalItems Produk)",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    formatRupiah(totalPrice),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2196F3)
                                )
                            }

                            Button(
                                onClick = onCheckoutClick,
                                modifier = Modifier
                                    .height(50.dp)
                                    .widthIn(min = 150.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50)),
                                shape = RoundedCornerShape(25.dp)
                            ) {
                                Icon(
                                    Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Checkout",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top
            ) {
                // Product Image
                cartItem.product.imageRes?.let {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = cartItem.product.name,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Product Details
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        cartItem.product.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        cartItem.product.price,
                        color = Color(0xFF2196F3),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Total price for this item
                    Text(
                        "Total: ${formatRupiah(cartItem.totalPrice)}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                // Remove button
                IconButton(
                    onClick = onRemoveClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Hapus",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quantity controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Jumlah:",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Decrease button
                    IconButton(
                        onClick = onDecreaseQuantity,
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                if (cartItem.quantity > 1) Color(0xFFE3F2FD) else Color(0xFFF5F5F5),
                                CircleShape
                            )
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Kurangi",
                            tint = if (cartItem.quantity > 1) Color(0xFF2196F3) else Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // Quantity display
                    Text(
                        cartItem.quantity.toString(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.widthIn(min = 24.dp),
                        textAlign = TextAlign.Center
                    )

                    // Increase button
                    IconButton(
                        onClick = onIncreaseQuantity,
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFFE3F2FD), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Tambah",
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// Helper function to format currency
fun formatRupiah(amount: Int): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(amount).replace("IDR", "Rp")
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview() {
    val dummyCartViewModel = remember { CartViewModel() }.apply {
        addToCart(
            Product(
                name = "Mug UNIDA Premium",
                price = "Rp15.000",
                imageRes = R.drawable.p1,
                description = "Mug keramik dengan logo UNIDA",
                rating = 4.6
            )
        )
        addToCart(
            Product(
                name = "T-Shirt UNIDA",
                price = "Rp75.000",
                imageRes = R.drawable.p1,
                description = "Kaos premium UNIDA",
                rating = 4.8
            )
        )
    }

    CartScreen(
        navController = rememberNavController(),
        currentRoute = "cart",
        onNavigate = {},
        onCheckoutClick = {},
        cartViewModel = dummyCartViewModel
    )
}