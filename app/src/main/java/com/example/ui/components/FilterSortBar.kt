package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PriceRangeFilter
import com.example.model.PriceSortOrder
import com.example.ui.theme.GsmBorder
import com.example.ui.theme.GsmCyan
import com.example.ui.theme.GsmGold
import com.example.ui.theme.GsmGreen
import com.example.ui.theme.GsmSurface
import com.example.ui.theme.GsmSurfaceElevated
import com.example.ui.theme.GsmSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun FilterSortBar(
    currentSort: PriceSortOrder,
    currentRange: PriceRangeFilter,
    accentColor: Color,
    onSortChange: (PriceSortOrder) -> Unit,
    onRangeChange: (PriceRangeFilter) -> Unit,
    onOpenFilterSheet: () -> Unit,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSortMenuOpen by remember { mutableStateOf(false) }
    val isFilterActive = currentRange.id != "all" || currentSort != PriceSortOrder.DEFAULT

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Detailed Filter Button
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(if (isFilterActive) accentColor.copy(alpha = 0.18f) else GsmSurfaceElevated)
                .border(
                    width = if (isFilterActive) 1.5.dp else 1.dp,
                    color = if (isFilterActive) accentColor else GsmBorder,
                    shape = RoundedCornerShape(10.dp)
                )
                .clickable { onOpenFilterSheet() }
                .padding(horizontal = 10.dp, vertical = 7.dp)
                .testTag("filter_budget_btn"),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filter Budget",
                    tint = if (isFilterActive) accentColor else TextSecondary,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Budget",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isFilterActive) accentColor else TextPrimary
                )
                if (isFilterActive) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(accentColor)
                    )
                }
            }
        }

        // Sort Dropdown Button
        Box {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (currentSort != PriceSortOrder.DEFAULT) accentColor.copy(alpha = 0.15f) else GsmSurfaceElevated)
                    .border(
                        width = if (currentSort != PriceSortOrder.DEFAULT) 1.dp else 0.5.dp,
                        color = if (currentSort != PriceSortOrder.DEFAULT) accentColor else GsmBorder,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { isSortMenuOpen = true }
                    .padding(horizontal = 10.dp, vertical = 7.dp)
                    .testTag("sort_dropdown_btn")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Urutkan",
                        tint = if (currentSort != PriceSortOrder.DEFAULT) accentColor else TextSecondary,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentSort.shortLabel,
                        fontSize = 11.sp,
                        fontWeight = if (currentSort != PriceSortOrder.DEFAULT) FontWeight.Bold else FontWeight.Medium,
                        color = if (currentSort != PriceSortOrder.DEFAULT) accentColor else TextPrimary
                    )
                }
            }

            DropdownMenu(
                expanded = isSortMenuOpen,
                onDismissRequest = { isSortMenuOpen = false },
                modifier = Modifier.background(GsmSurface)
            ) {
                PriceSortOrder.values().forEach { sortItem ->
                    val isChosen = sortItem == currentSort
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = sortItem.label,
                                fontSize = 12.sp,
                                fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                color = if (isChosen) accentColor else TextPrimary
                            )
                        },
                        onClick = {
                            onSortChange(sortItem)
                            isSortMenuOpen = false
                        }
                    )
                }
            }
        }

        // Price Presets Quick Chips
        PriceRangeFilter.PRESETS.forEach { preset ->
            val isSelected = currentRange.id == preset.id
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) accentColor else GsmSurfaceVariant)
                    .border(
                        width = if (isSelected) 1.dp else 0.5.dp,
                        color = if (isSelected) accentColor else GsmBorder,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onRangeChange(preset) }
                    .padding(horizontal = 10.dp, vertical = 7.dp)
                    .testTag("price_chip_${preset.id}")
            ) {
                Text(
                    text = preset.label,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) GsmSurface else TextSecondary
                )
            }
        }

        // Reset Filter Button (Visible when any filter is active)
        if (isFilterActive) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(GsmSurfaceVariant)
                    .clickable { onResetFilters() }
                    .padding(horizontal = 8.dp, vertical = 7.dp)
                    .testTag("reset_filter_btn")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Reset",
                        tint = TextMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Reset",
                        fontSize = 10.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
