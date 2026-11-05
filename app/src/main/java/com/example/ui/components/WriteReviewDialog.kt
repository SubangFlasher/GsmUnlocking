package com.example.ui.components

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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.Product
import com.example.model.ProductType
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
fun WriteReviewDialog(
    product: Product,
    onDismiss: () -> Unit,
    onSubmit: (rating: Int, author: String, role: String, comment: String, deviceOrVariant: String) -> Unit
) {
    var selectedRating by remember { mutableIntStateOf(5) }
    var authorName by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Teknisi Counter") }
    var commentText by remember { mutableStateOf("") }
    var deviceOrVariantText by remember {
        mutableStateOf(
            if (product.type == ProductType.REMOTE) "Samsung A54 / UltraViewer"
            else product.variants.firstOrNull()?.name ?: "Lisensi 6 Bulan"
        )
    }

    val roles = listOf("Teknisi Counter", "Master Flasher", "Pelanggan Terverifikasi")

    val ratingLabels = mapOf(
        5 to "Luar Biasa! (Sangat Puas)",
        4 to "Bagus & Lancar",
        3 to "Cukup Baik",
        2 to "Kurang Puas",
        1 to "Bermasalah"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, GsmGold.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .testTag("write_review_dialog"),
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
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GsmGold.copy(alpha = 0.15f))
                                .border(1.dp, GsmGold.copy(alpha = 0.4f), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.RateReview, contentDescription = null, tint = GsmGold, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Beri Ulasan & Rating",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = product.name,
                                fontSize = 11.sp,
                                color = GsmCyan,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(30.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive Star Rating
                Text(
                    text = "Berapa bintang untuk layanan ini?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    (1..5).forEach { star ->
                        val isFilled = star <= selectedRating
                        IconButton(
                            onClick = { selectedRating = star },
                            modifier = Modifier
                                .size(44.dp)
                                .testTag("star_rate_$star")
                        ) {
                            Icon(
                                imageVector = if (isFilled) Icons.Filled.Star else Icons.Outlined.Star,
                                contentDescription = "$star Bintang",
                                tint = if (isFilled) GsmGold else TextMuted,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                }

                Text(
                    text = ratingLabels[selectedRating] ?: "",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = GsmGold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Author Name
                OutlinedTextField(
                    value = authorName,
                    onValueChange = { authorName = it },
                    label = { Text("Nama atau Nama Counter Anda") },
                    placeholder = { Text("Contoh: Rian Cell / Bengkel Flashing") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("review_author_input"),
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

                // Role Chips
                Text(
                    text = "Kategori Pengguna:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    roles.forEach { role ->
                        val isSelected = selectedRole == role
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) GsmCyan.copy(alpha = 0.2f) else GsmSurfaceElevated)
                                .border(1.dp, if (isSelected) GsmCyan else GsmBorder, RoundedCornerShape(8.dp))
                                .clickable { selectedRole = role }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = role,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) GsmCyan else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Device or Variant Used
                OutlinedTextField(
                    value = deviceOrVariantText,
                    onValueChange = { deviceOrVariantText = it },
                    label = { Text(if (product.type == ProductType.REMOTE) "Tipe HP & Kendala Yang Dikerjakan" else "Paket Durasi Lisensi") },
                    placeholder = { Text("Contoh: Samsung A54 5G (FRP Done)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("review_device_input"),
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

                // Review Comment Body
                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    label = { Text("Pengalaman & Ulasan Lengkap") },
                    placeholder = { Text("Ceritakan kecepatan pengerjaan, stabilitas server, atau hasil bypass...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("review_comment_input"),
                    maxLines = 4,
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

                Spacer(modifier = Modifier.height(18.dp))

                // Submit Button
                Button(
                    onClick = {
                        val validAuthor = if (authorName.isBlank()) "Teknisi Mitra" else authorName.trim()
                        val validComment = if (commentText.isBlank()) "Pelayanan sangat cepat dan terpercaya. Rekomendasi teknisi!" else commentText.trim()
                        onSubmit(selectedRating, validAuthor, selectedRole, validComment, deviceOrVariantText.trim())
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_review_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GsmGold,
                        contentColor = GsmBackground
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Kirim Ulasan & Rating", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
