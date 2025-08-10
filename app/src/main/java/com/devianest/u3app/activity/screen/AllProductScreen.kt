package com.devianest.u3app.activity.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devianest.u3app.R
import com.devianest.u3app.activity.component.Product
import com.devianest.u3app.activity.viewmodel.CartViewModel
import com.devianest.u3app.uiProject.theme.U3AppTheme
import com.devianest.u3app.uiProject.theme.black
import com.devianest.u3app.uiProject.theme.blue
import kotlinx.coroutines.launch

// 2. Updated AllProductScreen.kt
// Modifikasi fungsi ProductItemCard dan AllProductScreen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllProductScreen(
    onBack: () -> Unit,
    navController: NavController,
    cartViewModel: CartViewModel,
    targetCategory: String? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Data produk per kategori (menggunakan data yang sama dengan dokumen Anda)
    val merchandise = listOf(
        Product(name = "Kaos 100 Tahun Gontor", price = "Rp100.000", description = "Kaos edisi spesial 100 tahun", rating = 4.9, imageRes = R.drawable.p9),
        Product("Jersey Santai", "Rp110.000", null, "Dibuat dengan bahan berkualitas", 4.9, R.drawable.m2),
        Product("Kitset Notebook", "Rp250.000", null, "Notebook-Thumbler-Pen", 4.8, R.drawable.m3),
        Product("Tote Bag", "Rp36.000", null, "Tas jinjing serbaguna", 4.8, R.drawable.m4),
        Product("Mug UNIDA", "Rp30.000", null, "Mug keramik dengan logo UNIDA", 4.9, R.drawable.m5),
        Product("Thumbler Kece", "Rp45.000", null, "Cocok untuk genZ", 4.8, R.drawable.m6),
        Product("Gantungan Kunci Gontor", "Rp20.000", null, "dibuat dengan kulit asli", 4.7, R.drawable.m7),
        Product("Topi Keren", "Rp55.000", null, "menghindari panas berlebih", 4.9, R.drawable.m8),
        Product("Jam Dinding", "Rp60.000", null, "Melatih disiplin", 4.8, R.drawable.m9),
        Product("Buku Peradaban", "Rp90.000", null, "Pedoman sejarah", 4.2, R.drawable.m10)
    )

    val bakery = listOf(
        Product("Cromboloni", "Rp9.000", null, "Roti croissant dengan filling", 4.9, R.drawable.b1),
        Product("Pizza Bulat", "Rp7.000", null, "Donat dengan glazur manis", 4.8, R.drawable.b2),
        Product("Roti Abon", "Rp8.000", null, "Roti lembut rasa coklat", 4.9, R.drawable.b3),
        Product("Roti Gulung", "Rp9.000", null, "Kue kecil dengan frosting", 4.9, R.drawable.b4),
        Product("Roti isi kacang merah", "Rp7.000", null, "Roti bundar dengan lubang", 4.9, R.drawable.b5),
        Product("Roti Sosis", "Rp6.000", null, "Pastry Denmark yang lembut", 4.8, R.drawable.b6),
        Product("Roti Coklat Keju", "Rp7.000", null, "Pastry Denmark yang lembut", 4.8, R.drawable.b7),
        Product("Burger", "Rp12.000", null, "Pastry Denmark yang lembut", 4.8, R.drawable.b8),
        Product("Salad Buah", "Rp11.000", null, "Pastry Denmark yang lembut", 4.8, R.drawable.b9),
        Product("Roti Boy", "Rp8.000", null, "Pastry Denmark yang lembut", 4.8, R.drawable.b10),
        Product("Dessert Box", "Rp15.000", null, "Pastry Denmark yang lembut", 4.8, R.drawable.b11),
        Product("Kue Tart Custom", "Rp18.000", null, "Teh hijau dengan susu", 4.8, R.drawable.v8)
    )

    val beverage = listOf(
        Product("Cheese Milk", "Rp10.000", null, "Teh manis dingin segar", 4.8, R.drawable.v1),
        Product("Cappucino", "Rp8.000", null, "Kopi hitam original", 4.8, R.drawable.v2),
        Product("Infuse Water", "Rp10.000", null, "Jus jeruk segar asli", 4.6, R.drawable.v3),
        Product("Fruit Jus", "Rp9.000", null, "Susu kocok rasa vanila", 4.9, R.drawable.v4),
        Product("Puding", "Rp7.000", null, "Smoothie buah campur", 4.7, R.drawable.v7),
        Product("All Variant Drink", "Rp18.000", null, "Teh hijau dengan susu", 4.8, R.drawable.v9),
        Product("MilkShake", "Rp18.000", null, "Teh hijau dengan susu", 4.8, R.drawable.v10)
    )

    val boxRice = listOf(
        Product("Large Mika Rice Box-4 lauk", "Rp20.000", null, "Nasi dengan ayam goreng", 4.8, R.drawable.r1),
        Product("Small Mika Rice Box-2 lauk", "Rp15.000", null, "Nasi dengan rendang sapi", 4.9, R.drawable.r2),
        Product("Rice Box-1 lauk", "Rp10.000", null, "Nasi dengan ikan bakar", 4.7, R.drawable.r3),
        Product("Rice Bowl-1 lauk", "Rp10.000", null, "Nasi dengan sayur campur", 4.5, R.drawable.r4),
        Product("Jumbo Rice Box-4 lauk + Pisang", "Rp25.000", null, "Nasi dengan gudeg jogja", 4.8, R.drawable.r5)
    )

    val carRent = listOf(
        Product("Apv", "Rp500.000", null, "Bus mini 16 seat", 4.6, R.drawable.l3),
        Product("Hiace", "Rp800.000", null, "Mobil keluarga 7 seat", 4.7, R.drawable.l1),
        Product("Elf", "Rp1.000.000", null, "Mobil premium 8 seat", 4.8, R.drawable.l2),
        Product("Bus", "Rp2.000.000", null, "Bus kecil 19 seat", 4.7, R.drawable.l4)
    )

    val categories = listOf(
        "Merchandise" to merchandise,
        "Bakery Product" to bakery,
        "Beverage Product" to beverage,
        "Box Rice" to boxRice,
        "Car Rent" to carRent
    )

    // Auto scroll logic sama seperti sebelumnya
    LaunchedEffect(targetCategory) {
        if (targetCategory != null) {
            val targetIndex = categories.indexOfFirst { it.first == targetCategory }
            if (targetIndex >= 0) {
                val scrollPosition = targetIndex * 2
                coroutineScope.launch {
                    listState.animateScrollToItem(scrollPosition)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    Text(
                        if (targetCategory != null) targetCategory else "Our Product",
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2191DF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        // Tambahkan snackbarHost dengan custom styling
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = Color(0xFF4CAF50), // Hijau untuk sukses
                        contentColor = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            categories.forEach { (title, products) ->
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = if (title == targetCategory) Color(0xFF2191DF) else Color.Black
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 800.dp)
                    ) {
                        items(products) { product ->
                            // Update ProductItemCard untuk tidak pindah halaman
                            ProductItemCard(
                                product = product,
                                cartViewModel = cartViewModel,
                                onAddToCart = {
                                    // Hapus navigasi ke cart, hanya tampilkan snackbar
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "✅ ${product.name} telah ditambahkan ke keranjang!",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(20.dp)) }
                    }
                }
            }
        }
    }
}

// Update ProductItemCard untuk tidak navigate
@Composable
fun ProductItemCard(
    product: Product,
    cartViewModel: CartViewModel,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .width(165.dp)
            .height(260.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = product.imageRes ?: R.drawable.profile1),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = product.price,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF388E3C)
            )
            // Fix: Convert Double to Float
            StarRating(product.rating.toFloat())
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = {
                    cartViewModel.addToCart(product)
                    onAddToCart() // Hanya panggil callback untuk snackbar
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = blue)
            ) {
                Text("+", fontSize = 20.sp, color = black)
            }
        }
    }
}

// StarRating composable function (if not already defined elsewhere)
@Composable
fun StarRating(rating: Float) {
    Row {
        repeat(5) { index ->
            val starIcon = when {
                index < rating.toInt() -> Icons.Filled.Star
                index < rating && rating % 1 >= 0.5f -> Icons.Filled.StarHalf
                else -> Icons.Outlined.StarBorder
            }
            Icon(
                imageVector = starIcon,
                contentDescription = null,
                tint = Color(0xFFFFD700), // Gold color
                modifier = Modifier.size(16.dp)
            )
        }
    }
}