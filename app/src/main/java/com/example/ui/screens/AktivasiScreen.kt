package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.PriceRangeFilter
import com.example.model.PriceSortOrder
import com.example.model.Product
import com.example.model.ProductVariant
import com.example.ui.components.FilterSortBar
import com.example.ui.components.ProductCard
import com.example.ui.theme.GsmBackground
import com.example.ui.theme.GsmBorder
import com.example.ui.theme.GsmCyan
import com.example.ui.theme.GsmGreen
import com.example.ui.theme.GsmSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun AktivasiScreen(
    products: List<Product>,
    searchQuery: String,
    selectedCategory: String,
    currentSort: PriceSortOrder,
    currentRange: PriceRangeFilter,
    onCategoryChange: (String) -> Unit,
    onSortChange: (PriceSortOrder) -> Unit,
    onRangeChange: (PriceRangeFilter) -> Unit,
    onOpenFilterSheet: () -> Unit,
    onResetFilters: () -> Unit,
    onDetailClick: (Product) -> Unit,
    onOrderClick: (Product, ProductVariant) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf("Semua", "Multi-Brand", "Samsung & Flagship", "MTK & Unisoc", "Xiaomi Specialist", "Hardware & Skema", "Dongle Digital")

    val minPrice = currentRange.minPrice
    val maxPrice = currentRange.maxPrice

    val filteredProducts = products.filter { prod ->
        val matchesCategory = selectedCategory == "Semua" || prod.category.equals(selectedCategory, ignoreCase = true)
        val matchesQuery = searchQuery.isBlank() ||
                prod.name.contains(searchQuery, ignoreCase = true) ||
                prod.subtitle.contains(searchQuery, ignoreCase = true) ||
                prod.description.contains(searchQuery, ignoreCase = true) ||
                prod.supportedBrands.any { it.contains(searchQuery, ignoreCase = true) }
        val startingPrice = prod.variants.minOfOrNull { it.price } ?: 0L
        val matchesPrice = startingPrice in minPrice..maxPrice
        matchesCategory && matchesQuery && matchesPrice
    }.let { list ->
        when (currentSort) {
            PriceSortOrder.DEFAULT -> list
            PriceSortOrder.PRICE_LOW_TO_HIGH -> list.sortedBy { prod -> prod.variants.minOfOrNull { it.price } ?: 0L }
            PriceSortOrder.PRICE_HIGH_TO_LOW -> list.sortedByDescending { prod -> prod.variants.minOfOrNull { it.price } ?: 0L }
            PriceSortOrder.NAME_ASC -> list.sortedBy { it.name }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(GsmBackground)
            .testTag("aktivasi_screen"),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Promo Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, GsmCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hero_banner),
                    contentDescription = "Promo Banner",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Gradient Overlay
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    GsmBackground.copy(alpha = 0.92f),
                                    GsmBackground.copy(alpha = 0.65f),
                                    GsmBackground.copy(alpha = 0.2f)
                                )
                            )
                        )
                )

                // Text Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GsmCyan.copy(alpha = 0.2f))
                            .border(0.5.dp, GsmCyan, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.FlashOn, contentDescription = null, tint = GsmCyan, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = "PROSES INSTAN 5 MENIT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GsmCyan)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "PUSAT AKTIVASI TOOL RESMI",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )

                    Text(
                        text = "UnlockTool • Chimera • Pandora • Borneo • DFT PRO",
                        fontSize = 11.sp,
                        color = GsmCyan,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null, tint = GsmGreen, modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Garansi Server Anti Banned • Akun 100% Original", fontSize = 10.sp, color = GsmGreen)
                    }
                }
            }
        }

        // Budget & Sort Filter Bar
        item {
            FilterSortBar(
                currentSort = currentSort,
                currentRange = currentRange,
                accentColor = GsmCyan,
                onSortChange = onSortChange,
                onRangeChange = onRangeChange,
                onOpenFilterSheet = onOpenFilterSheet,
                onResetFilters = onResetFilters
            )
        }

        // Category Filter Chips
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) GsmCyan else GsmSurfaceElevated)
                            .border(1.dp, if (isSelected) GsmCyan else GsmBorder, RoundedCornerShape(10.dp))
                            .clickable { onCategoryChange(category) }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                            .testTag("cat_chip_$category")
                    ) {
                        Text(
                            text = category,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) GsmBackground else TextSecondary
                        )
                    }
                }
            }
        }

        // Product Cards
        if (filteredProducts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Tidak Ada Tool Sesuai Filter Budget", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Coba ubah rentang harga atau reset filter pencarian",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onResetFilters,
                            colors = ButtonDefaults.buttonColors(containerColor = GsmSurfaceElevated),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, tint = GsmCyan, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Reset Filter Budget", fontSize = 12.sp, color = GsmCyan, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(filteredProducts, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    onDetailClick = { onDetailClick(product) },
                    onOrderClick = { variant -> onOrderClick(product, variant) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
