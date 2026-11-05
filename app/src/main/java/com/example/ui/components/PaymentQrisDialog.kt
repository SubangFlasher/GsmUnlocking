package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.OrderRecord
import com.example.model.PaymentChannel
import com.example.ui.theme.GsmBackground
import com.example.ui.theme.GsmBorder
import com.example.ui.theme.GsmCyan
import com.example.ui.theme.GsmGold
import com.example.ui.theme.GsmGreen
import com.example.ui.theme.GsmRed
import com.example.ui.theme.GsmSurface
import com.example.ui.theme.GsmSurfaceElevated
import com.example.ui.theme.GsmSurfaceVariant
import com.example.ui.theme.QrisColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GsmViewModel
import kotlinx.coroutines.delay

@Composable
fun PaymentQrisDialog(
    order: OrderRecord,
    onDismiss: () -> Unit,
    onConfirmPayment: (String) -> Unit
) {
    val context = LocalContext.current
    var secondsLeft by remember { mutableIntStateOf(900) } // 15 mins

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft -= 1
        }
    }

    val minutes = secondsLeft / 60
    val secs = secondsLeft % 60
    val timerText = String.format("%02d:%02d", minutes, secs)

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label disalin!", Toast.LENGTH_SHORT).show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, GsmCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .testTag("payment_qris_dialog"),
            colors = CardDefaults.cardColors(containerColor = GsmSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Close Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "PEMBAYARAN RESMI",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GsmCyan,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = order.paymentChannel.displayName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Countdown Timer Box
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(GsmRed.copy(alpha = 0.15f))
                        .border(1.dp, GsmRed.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = GsmRed, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Selesaikan dalam: $timerText",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = GsmRed
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // QR Code Matrix Simulation
                if (order.paymentChannel == PaymentChannel.QRIS) {
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "QRIS",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = QrisColor,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "NMID: ID1020083921021",
                                    fontSize = 9.sp,
                                    color = Color.DarkGray
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            // Composed Matrix
                            Canvas(modifier = Modifier.size(160.dp)) {
                                drawQrMatrix(size.width, size.height)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Scan QRIS pakai BCA, Mandiri, Dana, GoPay, OVO, ShopeePay",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                } else {
                    // Bank / E-Wallet Info Card
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(GsmSurfaceElevated)
                            .border(1.dp, GsmBorder, RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(text = "Nomor Rekening / Virtual Account:", fontSize = 11.sp, color = TextMuted)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = order.paymentChannel.accountNumber,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = GsmCyan
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(GsmSurfaceVariant)
                                        .clickable { copyToClipboard("No Rekening", order.paymentChannel.accountNumber) }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = GsmCyan, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "Salin", fontSize = 11.sp, color = GsmCyan, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Atas Nama: ${order.paymentChannel.accountHolder}", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Nominal Bayar Card with Unique Code
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(GsmSurfaceVariant)
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "Total yang Harus Ditransfer:", fontSize = 11.sp, color = TextMuted)
                                Text(
                                    text = GsmViewModel.formatRupiah(order.total),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = GsmGold
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(GsmSurfaceElevated)
                                    .clickable { copyToClipboard("Nominal Bayar", order.total.toString()) }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = GsmGold, modifier = Modifier.size(13.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Salin Jumlah", fontSize = 11.sp, color = GsmGold, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "⚠ Pastikan transfer tepat sampai 3 digit terakhir (+Rp ${order.uniqueCode}) agar sistem verifikasi otomatis.",
                            fontSize = 10.sp,
                            color = GsmGold.copy(alpha = 0.9f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Action Buttons
                Button(
                    onClick = { onConfirmPayment(order.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("confirm_paid_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = GsmGreen, contentColor = GsmBackground),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Saya Sudah Bayar (Cek Otomatis)", fontSize = 13.sp, fontWeight = FontWeight.Black)
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        val message = "Halo Admin GSM UNLOCKING INDONESIA, saya sudah transfer untuk pesanan:\n" +
                                "ID: ${order.id}\n" +
                                "Produk: ${order.items.firstOrNull()?.product?.name}\n" +
                                "Total: ${GsmViewModel.formatRupiah(order.total)}\n" +
                                "Mohon diproses."
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/6281234567890?text=${Uri.encode(message)}"))
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {
                            Toast.makeText(context, "Membuka WhatsApp CS...", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("whatsapp_confirm_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null, tint = GsmGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Kirim Bukti ke WhatsApp Admin", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

// Custom Draw function for realistic QR matrix pattern
fun androidx.compose.ui.graphics.drawscope.DrawScope.drawQrMatrix(width: Float, height: Float) {
    val cellSize = width / 25f
    val black = Color.Black

    // Draw 3 corner position squares
    fun drawFinder(cx: Float, cy: Float) {
        // Outer black 7x7
        drawRect(black, topLeft = Offset(cx, cy), size = Size(cellSize * 7, cellSize * 7))
        // Inner white 5x5
        drawRect(Color.White, topLeft = Offset(cx + cellSize, cy + cellSize), size = Size(cellSize * 5, cellSize * 5))
        // Center black 3x3
        drawRect(black, topLeft = Offset(cx + cellSize * 2, cy + cellSize * 2), size = Size(cellSize * 3, cellSize * 3))
    }

    drawFinder(0f, 0f)
    drawFinder(width - cellSize * 7, 0f)
    drawFinder(0f, height - cellSize * 7)

    // Data matrix pseudo bits
    val pattern = intArrayOf(
        0b10101010, 0b11001100, 0b11110000, 0b01010101, 0b10010011,
        0b11011010, 0b00110101, 0b11001001, 0b10101111, 0b01010011,
        0b10110101, 0b01101100, 0b10011011, 0b11010010, 0b01001101
    )

    for (row in 8..16) {
        for (col in 0..24) {
            val bit = (pattern[(row * 7 + col) % pattern.size] shr (col % 8)) and 1
            if (bit == 1) {
                drawRect(
                    black,
                    topLeft = Offset(col * cellSize, row * cellSize),
                    size = Size(cellSize, cellSize)
                )
            }
        }
    }

    for (row in 0..6) {
        for (col in 8..16) {
            val bit = (pattern[(row * 3 + col) % pattern.size] shr (col % 8)) and 1
            if (bit == 1) {
                drawRect(
                    black,
                    topLeft = Offset(col * cellSize, row * cellSize),
                    size = Size(cellSize, cellSize)
                )
            }
        }
    }

    for (row in 18..24) {
        for (col in 8..24) {
            val bit = (pattern[(row * 5 + col) % pattern.size] shr (col % 8)) and 1
            if (bit == 1) {
                drawRect(
                    black,
                    topLeft = Offset(col * cellSize, row * cellSize),
                    size = Size(cellSize, cellSize)
                )
            }
        }
    }
}
