// 1. Updated MainScreen.kt with Functional Search
package com.devianest.u3app.activity.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.devianest.u3app.R
import com.devianest.u3app.activity.component.BottomNavigationBar
import com.devianest.u3app.activity.component.Product
import com.devianest.u3app.uiProject.theme.U3AppTheme
import com.devianest.u3app.uiProject.theme.black
import com.devianest.u3app.uiProject.theme.blue
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import com.devianest.u3app.activity.viewmodel.CartViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSeeMoreClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
    onCategoryClick: (String) -> Unit = {},
    currentRoute: String = "MainScreen",
    cartViewModel: CartViewModel
) {
    // State untuk search
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    // Tambahkan SnackbarHostState dan CoroutineScope
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Daftar semua produk untuk pencarian
    val allProducts = remember {
        listOf(
            // Merchandise
            Product(name = "Kaos 100 Tahun Gontor", price = "Rp100.000", description = "Kaos edisi spesial 100 tahun", rating = 4.9, imageRes = R.drawable.p9),
            Product("Jersey Santai", "Rp110.000", null, "Dibuat dengan bahan berkualitas", 4.9, R.drawable.m2),
            Product("Kitset Notebook", "Rp250.000", null, "Notebook-Thumbler-Pen", 4.8, R.drawable.m3),
            Product("Tote Bag", "Rp36.000", null, "Tas jinjing serbaguna", 4.8, R.drawable.m4),
            Product("Mug UNIDA", "Rp30.000", null, "Mug keramik dengan logo UNIDA", 4.9, R.drawable.m5),
            Product("Thumbler Kece", "Rp45.000", null, "Cocok untuk genZ", 4.8, R.drawable.m6),
            Product("Gantungan Kunci Gontor", "Rp20.000", null, "dibuat dengan kulit asli", 4.7, R.drawable.m7),
            Product("Topi Keren", "Rp55.000", null, "menghindari panas berlebih", 4.9, R.drawable.m8),
            Product("Jam Dinding", "Rp60.000", null, "Melatih disiplin", 4.8, R.drawable.m9),
            Product("Buku Peradaban", "Rp90.000", null, "Pedoman sejarah", 4.2, R.drawable.m10),

            // Bakery
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
            Product("Kue Tart Custom", "Rp18.000", null, "Teh hijau dengan susu", 4.8, R.drawable.v8),

            // Beverage
            Product("Cheese Milk", "Rp10.000", null, "Teh manis dingin segar", 4.8, R.drawable.v1),
            Product("Cappucino", "Rp8.000", null, "Kopi hitam original", 4.8, R.drawable.v2),
            Product("Infuse Water", "Rp10.000", null, "Jus jeruk segar asli", 4.6, R.drawable.v3),
            Product("Fruit Jus", "Rp9.000", null, "Susu kocok rasa vanila", 4.9, R.drawable.v4),
            Product("Puding", "Rp7.000", null, "Smoothie buah campur", 4.7, R.drawable.v7),
            Product("All Variant Drink", "Rp18.000", null, "Teh hijau dengan susu", 4.8, R.drawable.v9),
            Product("MilkShake", "Rp18.000", null, "Teh hijau dengan susu", 4.8, R.drawable.v10),

            // Box Rice
            Product("Large Mika Rice Box-4 lauk", "Rp20.000", null, "Nasi dengan ayam goreng", 4.8, R.drawable.r1),
            Product("Small Mika Rice Box-2 lauk", "Rp15.000", null, "Nasi dengan rendang sapi", 4.9, R.drawable.r2),
            Product("Rice Box-1 lauk", "Rp10.000", null, "Nasi dengan ikan bakar", 4.7, R.drawable.r3),
            Product("Rice Bowl-1 lauk", "Rp10.000", null, "Nasi dengan sayur campur", 4.5, R.drawable.r4),
            Product("Jumbo Rice Box-4 lauk + Pisang", "Rp25.000", null, "Nasi dengan gudeg jogja", 4.8, R.drawable.r5),

            // Car Rent
            Product("Apv", "Rp500.000", null, "Bus mini 16 seat", 4.6, R.drawable.l3),
            Product("Hiace", "Rp800.000", null, "Mobil keluarga 7 seat", 4.7, R.drawable.l1),
            Product("Elf", "Rp1.000.000", null, "Mobil premium 8 seat", 4.8, R.drawable.l2),
            Product("Bus", "Rp2.000.000", null, "Bus kecil 19 seat", 4.7, R.drawable.l4)
        )
    }

    // Filter produk berdasarkan search query
    val filteredProducts = remember(searchQuery) {
        if (searchQuery.isEmpty()) {
            emptyList()
        } else {
            allProducts.filter { product ->
                product.name.contains(searchQuery, ignoreCase = true) ||
                        product.description?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    // Update isSearching berdasarkan search query
    LaunchedEffect(searchQuery) {
        isSearching = searchQuery.isNotEmpty()
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text("U3 Store", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = {  }) {
                        Icon(
                            painter = painterResource(id = R.drawable.logo2),
                            contentDescription = "Logo",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2191DF),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = onNavigate
            )
        },
        // Tambahkan snackbarHost
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = Color(0xFF4CAF50), // Warna hijau untuk sukses
                        contentColor = Color.White
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(0.dp)) }

            item {
                SearchBar(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    onClearSearch = { searchQuery = "" }
                )
            }

            // Tampilkan hasil pencarian jika sedang searching
            if (isSearching) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Hasil Pencarian \"$searchQuery\"",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(
                            onClick = { searchQuery = "" }
                        ) {
                            Text("Clear", color = Color.Red)
                        }
                    }
                }

                if (filteredProducts.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "❌ Tidak Ada Hasil",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Produk yang Anda cari tidak ditemukan",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    item {
                        SearchResultsSection(
                            products = filteredProducts,
                            cartViewModel = cartViewModel,
                            snackbarHostState = snackbarHostState,
                            coroutineScope = coroutineScope
                        )
                    }
                }
            } else {
                // Tampilkan konten normal jika tidak sedang searching
                item { BannerSection() }

                item { CategorySection(onCategoryClick = onCategoryClick) }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Best Product", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        TextButton(onClick = onSeeMoreClick) {
                            Text("See More")
                        }
                    }
                }

                // Pass snackbarHostState dan coroutineScope ke BestProductSection
                item {
                    BestProductSection(
                        cartViewModel = cartViewModel,
                        snackbarHostState = snackbarHostState,
                        coroutineScope = coroutineScope
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = { Text("Search Our Product...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = onClearSearch) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear search")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color(0xFFF0F0F0),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            textColor = Color.Black,
            placeholderColor = Color.Gray
        ),
        singleLine = true
    )
}

@Composable
fun SearchResultsSection(
    products: List<Product>,
    cartViewModel: CartViewModel,
    snackbarHostState: SnackbarHostState,
    coroutineScope: kotlinx.coroutines.CoroutineScope
) {
    LazyRow {
        items(products) { product ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .width(165.dp)
                    .height(260.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    if (product.imageRes != null) {
                        Image(
                            painter = painterResource(id = product.imageRes),
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                        )
                    } else if (product.imageUrl != null) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                        )
                    }

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

                    StarRating(product.rating.toFloat())

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            cartViewModel.addToCart(product)
                            // Tampilkan snackbar tanpa pindah halaman
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "✅ ${product.name} telah ditambahkan ke keranjang!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = blue)
                    ) {
                        Text("+", fontSize = 20.sp, color = black)
                    }
                }
            }
        }
    }
}

@Composable
fun BannerSection() {
    Image(
        painter = painterResource(id = R.drawable.banner),
        contentDescription = "Banner",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(Color.LightGray, RoundedCornerShape(12.dp))
    )
}

@Composable
fun CategorySection(onCategoryClick: (String) -> Unit) {
    val categories = listOf(
        "Merchandise" to R.drawable.p9,
        "Bakery" to R.drawable.p10,
        "Beverage" to R.drawable.v4,
        "Boxed Rice" to R.drawable.r2,
        "Rent Car" to R.drawable.l2
    )

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Category",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow {
            items(categories) { (categoryName, imageRes) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clickable {
                            val targetCategory = when(categoryName) {
                                "Merchandise" -> "Merchandise"
                                "Bakery" -> "Bakery Product"
                                "Beverage" -> "Beverage Product"
                                "Boxed Rice" -> "Box Rice"
                                "Rent Car" -> "Car Rent"
                                else -> categoryName
                            }
                            onCategoryClick(targetCategory)
                        }
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = categoryName,
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = categoryName, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun BestProductSection(
    cartViewModel: CartViewModel,
    snackbarHostState: SnackbarHostState,
    coroutineScope: kotlinx.coroutines.CoroutineScope
) {
    val products = listOf(
        Product(name = "Kaos 100 Tahun Gontor", price = "Rp100.000", description = "Kaos edisi spesial 100 tahun", rating = 4.9, imageRes = R.drawable.p9),
        Product(name = "Cromboloni", price = "Rp9.000", description = "rasa yang spektakuler", rating = 4.9, imageRes = R.drawable.b1),
        Product(name = "Cheese Milk Strawberry", price = "Rp8.000", description = "rasa keju dan susu menjadi satu", rating = 4.8, imageRes = R.drawable.v1),
        Product(name = "Mika Rice Box", price = "Rp20.000", description = "kelezatan yang tidak ada duanya", rating = 4.7, imageRes = R.drawable.r1),
        Product(name = "Hiace Rent-16 orang", price = "Rp800.000", description = "Kenyamanan pelanggan nomor 1", rating = 4.8, imageRes = R.drawable.l1),
        Product(name = "Fruit Salad", price = "Rp12.000", description = "Salad segar dan sehat", rating = 4.7, imageRes = R.drawable.b9)
    )

    LazyRow {
        items(products) { product ->
            Card(
                modifier = Modifier
                    .padding(8.dp)
                    .width(165.dp)
                    .height(260.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    if (product.imageRes != null) {
                        Image(
                            painter = painterResource(id = product.imageRes),
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                        )
                    } else if (product.imageUrl != null) {
                        AsyncImage(
                            model = product.imageUrl,
                            contentDescription = product.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                        )
                    }

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

                    StarRating(product.rating.toFloat())

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            cartViewModel.addToCart(product)
                            // Tampilkan snackbar tanpa pindah halaman
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "✅ ${product.name} telah ditambahkan ke keranjang!",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = blue)
                    ) {
                        Text("+", fontSize = 20.sp, color = black)
                    }
                }
            }
        }
    }
}

@Composable
fun StarRating(rating: Float, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        repeat(5) { index ->
            val icon = when {
                rating >= index + 1 -> Icons.Filled.Star
                rating > index -> Icons.Filled.StarHalf
                else -> Icons.Filled.StarBorder
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(String.format("%.1f", rating), fontSize = 12.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    val fakeCartViewModel = CartViewModel()
    U3AppTheme {
        MainScreen(cartViewModel = fakeCartViewModel)
    }
}