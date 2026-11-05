package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PriceRangeFilter
import com.example.model.PriceSortOrder
import com.example.ui.theme.GsmBackground
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
import com.example.viewmodel.GsmViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PriceFilterBottomSheet(
    currentSort: PriceSortOrder,
    currentRange: PriceRangeFilter,
    customMinPrice: Long?,
    customMaxPrice: Long?,
    accentColor: Color,
    onApply: (PriceSortOrder, PriceRangeFilter?, Long?, Long?) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var tempSort by remember { mutableStateOf(currentSort) }
    var tempRange by remember { mutableStateOf(currentRange) }
    var minPriceInput by remember { mutableStateOf(customMinPrice?.toString() ?: "") }
    var maxPriceInput by remember { mutableStateOf(customMaxPrice?.toString() ?: "") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = GsmSurface,
        dragHandle = null,
        modifier = Modifier.testTag("price_filter_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Kelola Budget & Urutkan",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp).testTag("close_filter_sheet_btn")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sort Options
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Sort, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Urutan Produk:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                PriceSortOrder.values().forEach { sortItem ->
                    val isSelected = tempSort == sortItem
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) accentColor.copy(alpha = 0.15f) else GsmSurfaceElevated)
                            .border(
                                width = if (isSelected) 1.5.dp else 0.5.dp,
                                color = if (isSelected) accentColor else GsmBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { tempSort = sortItem }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sortItem.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) accentColor else TextPrimary
                        )
                        if (isSelected) {
                            Text(text = "✓", fontSize = 13.sp, fontWeight = FontWeight.Black, color = accentColor)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = GsmBorder)
            Spacer(modifier = Modifier.height(14.dp))

            // Preset Price Ranges
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Paid, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Pilihan Budget Populer:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PriceRangeFilter.PRESETS.forEach { preset ->
                    val isChosen = tempRange.id == preset.id && minPriceInput.isBlank() && maxPriceInput.isBlank()
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isChosen) accentColor else GsmSurfaceElevated)
                            .border(
                                width = if (isChosen) 1.dp else 0.5.dp,
                                color = if (isChosen) accentColor else GsmBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable {
                                tempRange = preset
                                minPriceInput = ""
                                maxPriceInput = ""
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = preset.label,
                            fontSize = 11.sp,
                            fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                            color = if (isChosen) GsmSurface else TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Custom Price Range Inputs
            Text(
                text = "Atau Masukkan Rentang Harga Sendiri (Rp):",
                fontSize = 12.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = minPriceInput,
                    onValueChange = { minPriceInput = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Min (Rp)") },
                    placeholder = { Text("0", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f).testTag("input_min_price"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = defaultTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Text(text = "—", fontSize = 16.sp, color = TextMuted)

                OutlinedTextField(
                    value = maxPriceInput,
                    onValueChange = { maxPriceInput = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Maks (Rp)") },
                    placeholder = { Text("500000", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f).testTag("input_max_price"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = defaultTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Actions: Reset & Apply
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        tempSort = PriceSortOrder.DEFAULT
                        tempRange = PriceRangeFilter.PRESETS[0]
                        minPriceInput = ""
                        maxPriceInput = ""
                        onReset()
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("reset_filter_sheet_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Reset", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        val min = minPriceInput.toLongOrNull()
                        val max = maxPriceInput.toLongOrNull()
                        if (min != null || max != null) {
                            onApply(tempSort, null, min, max)
                        } else {
                            onApply(tempSort, tempRange, null, null)
                        }
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(48.dp)
                        .testTag("apply_filter_sheet_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = GsmBackground
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Terapkan Filter", fontSize = 13.sp, fontWeight = FontWeight.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
