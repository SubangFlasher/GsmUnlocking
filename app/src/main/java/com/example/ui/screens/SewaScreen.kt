package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PriceRangeFilter
import com.example.model.PriceSortOrder
import com.example.model.Product
import com.example.model.ProductVariant
import com.example.ui.components.FilterSortBar
import com.example.ui.components.ProductCard
import com.example.ui.theme.GsmBackground
import com.example.ui.theme.GsmBorder
import com.example.ui.theme.GsmGold
import com.example.ui.theme.GsmGreen
import com.example.ui.theme.GsmSurface
import com.example.ui.theme.GsmSurfaceElevated
import com.example.ui.theme.GsmSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun SewaScreen(
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
    val categories = listOf("Semua", "Sewa Populer", "Sewa Flagship", "Sewa MTK", "Sewa Xiaomi", "Sewa Skema")

    val minPrice = currentRange.minPrice
    val maxPrice = currentRange.maxPrice

    val filteredProducts = products.filter { prod ->
        val matchesCategory = selectedCategory == "Semua" || prod.category.equals(selectedCategory, ignoreCase = true)
        val matchesQuery = searchQuery.isBlank() ||
                prod.name.contains(searchQuery, ignoreCase = true) ||
                prod.subtitle.contains(searchQuery, ignoreCase = true) ||
                prod.description.contains(searchQuery, ignoreCase = true)
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
            .testTag("sewa_screen"),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Rental Header Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(GsmSurface)
                    .border(1.dp, GsmGold.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = GsmGold, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "RENTAL SERVER & DONGLE",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = GsmGold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(GsmGreen.copy(alpha = 0.15f))
                                .border(1.dp, GsmGreen.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(GsmGreen)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "18 SLOT SIAP", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GsmGreen)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Solusi praktis tanpa beli tool tahunan. Dapatkan akses akun server resmi per jam dengan harga mulai Rp 15.000.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Rules preview
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(GsmSurfaceElevated)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = GsmGold, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Login instan via username & password dikirim langsung di invoice",
                            fontSize = 11.sp,
                            color = TextPrimary
                        )
                    }
                }
            }
        }

        // Budget & Sort Filter Bar
        item {
            FilterSortBar(
                currentSort = currentSort,
                currentRange = currentRange,
                accentColor = GsmGold,
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
                            .background(if (isSelected) GsmGold else GsmSurfaceElevated)
                            .border(1.dp, if (isSelected) GsmGold else GsmBorder, RoundedCornerShape(10.dp))
                            .clickable { onCategoryChange(category) }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                            .testTag("sewa_cat_chip_$category")
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
                        Text(text = "Tidak Ada Akun Sewa Sesuai Budget", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Coba perluas rentang harga atau gunakan filter budget lain", fontSize = 12.sp, color = TextMuted)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onResetFilters,
                            colors = ButtonDefaults.buttonColors(containerColor = GsmSurfaceElevated),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, tint = GsmGold, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Reset Filter Budget", fontSize = 12.sp, color = GsmGold, fontWeight = FontWeight.Bold)
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
