package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.OrderRecord
import com.example.model.OrderStatus
import com.example.model.ProductType
import com.example.model.RemoteExecutionStatus
import com.example.ui.theme.GsmBackground
import com.example.ui.theme.GsmBorder
import com.example.ui.theme.GsmCyan
import com.example.ui.theme.GsmGold
import com.example.ui.theme.GsmGreen
import com.example.ui.theme.GsmRed
import com.example.ui.theme.GsmSurface
import com.example.ui.theme.GsmSurfaceElevated
import com.example.ui.theme.GsmSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GsmViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderHistoryDialog(
    orders: List<OrderRecord>,
    onDismiss: () -> Unit,
    onSelectOrder: (OrderRecord) -> Unit,
    onOpenRemoteTracker: ((OrderRecord) -> Unit)? = null
) {
    var selectedFilterIndex by remember { mutableIntStateOf(0) } // 0=Semua, 1=Remote Selesai, 2=Pembelian (Aktivasi/Sewa), 3=Dalam Proses
    var localSearchQuery by remember { mutableStateOf("") }

    val filterTabs = listOf("Semua", "Remote Service", "Lisensi & Sewa", "Proses")

    val filteredOrders = orders.filter { order ->
        val matchesCategory = when (selectedFilterIndex) {
            1 -> order.items.any { it.product.type == ProductType.REMOTE } || order.remoteTrackerData != null
            2 -> order.items.any { it.product.type == ProductType.AKTIVASI || it.product.type == ProductType.SEWA }
            3 -> order.status == OrderStatus.DIPROSES_SERVER || order.status == OrderStatus.MENUNGGU_PEMBAYARAN ||
                    order.remoteTrackerData?.status == RemoteExecutionStatus.IN_PROGRESS ||
                    order.remoteTrackerData?.status == RemoteExecutionStatus.PENDING
            else -> true
        }

        val matchesSearch = localSearchQuery.isBlank() ||
                order.id.contains(localSearchQuery, ignoreCase = true) ||
                order.items.any {
                    it.product.name.contains(localSearchQuery, ignoreCase = true) ||
                            it.selectedVariant.name.contains(localSearchQuery, ignoreCase = true) ||
                            it.customInfo.contains(localSearchQuery, ignoreCase = true)
                } ||
                (order.remoteTrackerData?.deviceModel?.contains(localSearchQuery, ignoreCase = true) == true) ||
                (order.remoteTrackerData?.assignedTechnician?.contains(localSearchQuery, ignoreCase = true) == true)

        matchesCategory && matchesSearch
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.90f)
                .clip(RoundedCornerShape(22.dp))
                .border(1.dp, GsmCyan.copy(alpha = 0.5f), RoundedCornerShape(22.dp))
                .testTag("order_history_dialog"),
            colors = CardDefaults.cardColors(containerColor = GsmSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GsmCyan.copy(alpha = 0.15f))
                                .border(1.dp, GsmCyan.copy(alpha = 0.3f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = GsmCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Riwayat Pesanan & Servis",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                            Text(
                                text = "${orders.size} Total Transaksi Tercatat",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("close_history_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar inside History
                OutlinedTextField(
                    value = localSearchQuery,
                    onValueChange = { localSearchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("history_search_input"),
                    placeholder = {
                        Text(text = "Cari ID pesanan, nama HP, atau tool...", fontSize = 12.sp, color = TextMuted)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (localSearchQuery.isNotEmpty()) {
                            IconButton(onClick = { localSearchQuery = "" }, modifier = Modifier.size(20.dp)) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Hapus", tint = TextSecondary, modifier = Modifier.size(14.dp))
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = GsmSurfaceElevated,
                        unfocusedContainerColor = GsmSurfaceElevated,
                        focusedBorderColor = GsmCyan,
                        unfocusedBorderColor = GsmBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Category Chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterTabs.indices.toList()) { index ->
                        val isSelected = selectedFilterIndex == index
                        val label = filterTabs[index]
                        val chipColor = when (index) {
                            1 -> GsmGreen // Remote
                            2 -> GsmGold // Lisensi
                            3 -> GsmCyan // Proses
                            else -> GsmCyan
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) chipColor.copy(alpha = 0.2f) else GsmSurfaceElevated)
                                .border(1.dp, if (isSelected) chipColor else GsmBorder, RoundedCornerShape(10.dp))
                                .clickable { selectedFilterIndex = index }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("history_filter_tab_$index")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                when (index) {
                                    1 -> Icon(imageVector = Icons.Default.SettingsRemote, contentDescription = null, tint = if (isSelected) chipColor else TextMuted, modifier = Modifier.size(13.dp))
                                    2 -> Icon(imageVector = Icons.Default.Key, contentDescription = null, tint = if (isSelected) chipColor else TextMuted, modifier = Modifier.size(13.dp))
                                    3 -> Icon(imageVector = Icons.Default.HourglassTop, contentDescription = null, tint = if (isSelected) chipColor else TextMuted, modifier = Modifier.size(13.dp))
                                    else -> Icon(imageVector = Icons.Default.FilterList, contentDescription = null, tint = if (isSelected) chipColor else TextMuted, modifier = Modifier.size(13.dp))
                                }
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) chipColor else TextSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Order List
                if (filteredOrders.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(imageVector = Icons.Default.History, contentDescription = null, tint = TextMuted, modifier = Modifier.size(44.dp))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = "Tidak Ada Riwayat Transaksi", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Belum ada pesanan yang sesuai dengan filter", fontSize = 12.sp, color = TextMuted)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredOrders, key = { it.id }) { order ->
                            val isRemoteOrder = order.items.any { it.product.type == ProductType.REMOTE } || order.remoteTrackerData != null
                            val remoteTracker = order.remoteTrackerData

                            val statusBadgeColor = when {
                                remoteTracker != null && remoteTracker.status == RemoteExecutionStatus.COMPLETED -> GsmGreen
                                remoteTracker != null && remoteTracker.status == RemoteExecutionStatus.IN_PROGRESS -> GsmCyan
                                remoteTracker != null && remoteTracker.status == RemoteExecutionStatus.PENDING -> GsmGold
                                order.status == OrderStatus.SELESAI_AKTIF -> GsmGreen
                                order.status == OrderStatus.DIPROSES_SERVER -> GsmGold
                                order.status == OrderStatus.MENUNGGU_PEMBAYARAN -> GsmCyan
                                else -> GsmRed
                            }

                            val statusText = when {
                                remoteTracker != null -> "Remote: ${remoteTracker.status.label}"
                                else -> order.status.label
                            }

                            val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("in", "ID")).format(Date(order.createdAt))

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .border(
                                        1.dp,
                                        if (remoteTracker?.status == RemoteExecutionStatus.IN_PROGRESS) GsmCyan.copy(alpha = 0.6f) else GsmBorder,
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable { onSelectOrder(order) }
                                    .testTag("order_item_${order.id}"),
                                colors = CardDefaults.cardColors(containerColor = GsmSurfaceElevated)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    // Row 1: Order ID, Type badge, Status pill
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            // Icon indicator
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(if (isRemoteOrder) GsmGreen.copy(alpha = 0.15f) else GsmCyan.copy(alpha = 0.15f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = if (isRemoteOrder) Icons.Default.SettingsRemote else Icons.Default.Key,
                                                    contentDescription = null,
                                                    tint = if (isRemoteOrder) GsmGreen else GsmCyan,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = order.id,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isRemoteOrder) GsmGreen else GsmCyan
                                            )
                                        }

                                        // Status Pill
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(statusBadgeColor.copy(alpha = 0.15f))
                                                .border(1.dp, statusBadgeColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 8.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = statusText,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = statusBadgeColor
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Row 2: Product Name & Variant
                                    Text(
                                        text = order.items.firstOrNull()?.product?.name ?: "Produk Layanan GSM",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    order.items.firstOrNull()?.selectedVariant?.let { variant ->
                                        val subText = if (variant.note.isNotBlank()) "Paket: ${variant.name} (${variant.note})" else "Paket: ${variant.name}"
                                        Text(
                                            text = subText,
                                            fontSize = 11.sp,
                                            color = TextSecondary
                                        )
                                    }

                                    // Remote specific details (Model & Technician)
                                    if (remoteTracker != null) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(GsmSurfaceVariant)
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(
                                                        text = "Unit: ${remoteTracker.deviceModel}",
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = TextPrimary,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                    Text(
                                                        text = "Teknisi: ${remoteTracker.assignedTechnician}",
                                                        fontSize = 10.sp,
                                                        color = GsmGreen
                                                    )
                                                }

                                                if (onOpenRemoteTracker != null) {
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(6.dp))
                                                            .background(GsmGreen.copy(alpha = 0.2f))
                                                            .clickable { onOpenRemoteTracker(order) }
                                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                                    ) {
                                                        Text(
                                                            text = "Live Tracker",
                                                            fontSize = 10.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            color = GsmGreen
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Row 3: Total price & Date
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = GsmViewModel.formatRupiah(order.total),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            color = GsmGold
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = dateStr,
                                                fontSize = 10.sp,
                                                color = TextMuted
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                imageVector = Icons.Default.ChevronRight,
                                                contentDescription = null,
                                                tint = TextMuted,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
