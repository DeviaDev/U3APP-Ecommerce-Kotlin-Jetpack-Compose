package com.devianest.u3app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.devianest.u3app.activity.component.Product
import com.devianest.u3app.activity.viewmodel.CartViewModel


@Composable
fun ProductItemCard(
    product: Product,
    cartViewModel: CartViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.name, style = MaterialTheme.typography.subtitle1)
            Text(text = "Rp${product.price}", style = MaterialTheme.typography.body1)

            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add to Cart",
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { cartViewModel.addToCart(product) }
            )
        }
    }
}
