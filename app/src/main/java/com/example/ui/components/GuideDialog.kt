package com.example.ui.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.HelpCenter
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

@Composable
fun GuideDialog(onDismiss: () -> Unit) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, GsmCyan.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .testTag("guide_dialog"),
            colors = CardDefaults.cardColors(containerColor = GsmSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.HelpCenter, contentDescription = null, tint = GsmCyan, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Panduan & Bantuan Teknisi",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Rule Sewa
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(GsmGold.copy(alpha = 0.1f))
                        .border(1.dp, GsmGold.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = GsmGold, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "ATURAN WAJIB SEWA AKUN / DONGLE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GsmGold)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "1. Dilarang login di 2 PC berbeda secara bersamaan.\n" +
                                    "2. Dilarang mengubah password/username akun server.\n" +
                                    "3. Durasi sewa dihitung otomatis sejak waktu pertama kali Anda login.\n" +
                                    "4. Jika sudah selesai, silakan klik LOGOUT di software agar slot dilepas.",
                            fontSize = 11.sp,
                            color = TextPrimary,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Remote Prep
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(GsmSurfaceElevated)
                        .padding(12.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Laptop, contentDescription = null, tint = GsmCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "PERSIAPAN REMOTE SERVICE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GsmCyan)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "• Download software UltraViewer (Rekomendasi) atau AnyDesk di PC Windows.\n" +
                                    "• Nonaktifkan sementara Windows Defender / Real-time Protection.\n" +
                                    "• Siapkan kabel data USB original & koneksi internet minimal 10 Mbps.\n" +
                                    "• Teknisi akan memandu colok kabel atau mode test point EDL / BROM.",
                            fontSize = 11.sp,
                            color = TextSecondary,
                            lineHeight = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Essential Drivers
                Text(text = "DRIVER UTAMA TEKNISI (SIAP PAKAI):", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))

                val drivers = listOf(
                    "Qualcomm QDLoader 9008 Driver (32/64 bit)",
                    "MediaTek MTK USB VCOM & LibUSB Filter",
                    "Samsung Mobile USB Driver v1.7.59",
                    "Unisoc / SPD Driver All-in-One",
                    "UltraViewer Setup v6.6 Resmi"
                )

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    drivers.forEach { driver ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(GsmSurfaceVariant)
                                .clickable {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${Uri.encode(driver)}"))
                                    context.startActivity(intent)
                                }
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = driver, fontSize = 11.sp, color = TextPrimary)
                            Icon(imageVector = Icons.Default.Download, contentDescription = null, tint = GsmCyan, modifier = Modifier.size(14.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // WA Contact Button
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/6281234567890?text=${Uri.encode("Halo CS GSM UNLOCKING INDONESIA, saya ingin konsultasi teknisi...")}"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("help_wa_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = GsmGreen, contentColor = GsmBackground),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Hubungi Helpdesk WhatsApp (24 Jam)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
