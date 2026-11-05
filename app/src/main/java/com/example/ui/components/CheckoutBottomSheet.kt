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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CartItem
import com.example.model.PaymentChannel
import com.example.model.Product
import com.example.model.ProductType
import com.example.model.ProductVariant
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutBottomSheet(
    targetProduct: Product?,
    targetVariant: ProductVariant?,
    cartItems: List<CartItem>,
    appliedCoupon: String,
    couponDiscount: Long,
    onApplyCoupon: (String) -> Unit,
    onDismiss: () -> Unit,
    onSubmitOrder: (name: String, wa: String, email: String, payment: PaymentChannel, customInfo: String, notes: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var name by remember { mutableStateOf("Arifin GSM Servis") }
    var whatsapp by remember { mutableStateOf("081288992341") }
    var email by remember { mutableStateOf("arifin.teknisi@gmail.com") }
    var customInfo by remember {
        mutableStateOf(
            when (targetProduct?.type) {
                ProductType.AKTIVASI -> "username: arifin_unlock / email: arifin.teknisi@gmail.com"
                ProductType.SEWA -> "PC: Laptop Teknisi Asus Win11"
                ProductType.REMOTE -> "Redmi Note 12 - UltraViewer: 28 991 201 pass: 8812"
                else -> ""
            }
        )
    }
    var notes by remember { mutableStateOf("") }
    var selectedPayment by remember { mutableStateOf(PaymentChannel.QRIS) }
    var couponInput by remember { mutableStateOf("") }

    val isSingleProduct = targetProduct != null && targetVariant != null
    val subtotal = if (isSingleProduct) {
        targetVariant!!.price
    } else {
        cartItems.sumOf { it.selectedVariant.price * it.quantity }
    }

    val uniqueCode = 188
    val finalTotal = (subtotal - couponDiscount + uniqueCode).coerceAtLeast(1000L)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = GsmSurface,
        dragHandle = null,
        modifier = Modifier.testTag("checkout_sheet")
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
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = GsmCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Formulir Pemesanan Resmi",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp).testTag("close_checkout_btn")
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Tutup", tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Order Summary Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GsmSurfaceElevated)
                    .border(1.dp, GsmBorder, RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "Ringkasan Produk:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    if (isSingleProduct) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = targetProduct!!.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Paket: ${targetVariant!!.name}",
                                    fontSize = 12.sp,
                                    color = GsmCyan
                                )
                            }
                            Text(
                                text = GsmViewModel.formatRupiah(targetVariant.price),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = GsmCyan
                            )
                        }
                    } else {
                        cartItems.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.product.name} (${item.selectedVariant.name})",
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = GsmViewModel.formatRupiah(item.selectedVariant.price * item.quantity),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Customer Details Form
            Text(
                text = "Data Teknisi / Pembeli:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nama Lengkap / Nama Konter") },
                modifier = Modifier.fillMaxWidth().testTag("input_customer_name"),
                colors = defaultTextFieldColors(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = whatsapp,
                onValueChange = { whatsapp = it },
                label = { Text("Nomor WhatsApp (Aktif)") },
                placeholder = { Text("0812xxxxxxxx") },
                modifier = Modifier.fillMaxWidth().testTag("input_customer_wa"),
                colors = defaultTextFieldColors(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Alamat Email") },
                placeholder = { Text("email@gmail.com") },
                modifier = Modifier.fillMaxWidth().testTag("input_customer_email"),
                colors = defaultTextFieldColors(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = customInfo,
                onValueChange = { customInfo = it },
                label = {
                    Text(
                        if (targetProduct?.type == ProductType.REMOTE) "Detail HP & ID UltraViewer / AnyDesk"
                        else if (targetProduct?.type == ProductType.AKTIVASI) "Username Akun & Email Tool"
                        else "Catatan / Identitas PC"
                    )
                },
                modifier = Modifier.fillMaxWidth().testTag("input_custom_info"),
                colors = defaultTextFieldColors(),
                shape = RoundedCornerShape(10.dp),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Methods
            Text(
                text = "Metode Pembayaran:",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PaymentChannel.values().forEach { channel ->
                    val isSelected = channel == selectedPayment
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) GsmCyan.copy(alpha = 0.12f) else GsmSurfaceElevated)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) GsmCyan else GsmBorder,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clickable { selectedPayment = channel }
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when (channel) {
                                    PaymentChannel.QRIS -> Icons.Default.QrCode2
                                    PaymentChannel.BCA, PaymentChannel.MANDIRI -> Icons.Default.AccountBalance
                                    else -> Icons.Default.Payment
                                },
                                contentDescription = null,
                                tint = if (isSelected) GsmCyan else TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = channel.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) GsmCyan else TextPrimary
                                )
                                Text(
                                    text = "Otomatis Verifikasi",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = GsmCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Promo Code Input
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = couponInput,
                    onValueChange = { couponInput = it },
                    placeholder = { Text("Kode Promo (e.g. GSMINDOPROMO)", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f).testTag("coupon_input"),
                    colors = defaultTextFieldColors(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onApplyCoupon(couponInput) },
                    modifier = Modifier.height(52.dp).testTag("apply_coupon_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = GsmSurfaceVariant),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = "Pakai", fontSize = 12.sp, color = GsmCyan, fontWeight = FontWeight.Bold)
                }
            }

            if (appliedCoupon.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Voucher $appliedCoupon aktif: Diskon Rp 25.000",
                    fontSize = 11.sp,
                    color = GsmGreen,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price Breakdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GsmSurfaceVariant.copy(alpha = 0.5f))
                    .padding(14.dp)
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Subtotal Produk", fontSize = 12.sp, color = TextSecondary)
                        Text(text = GsmViewModel.formatRupiah(subtotal), fontSize = 12.sp, color = TextPrimary)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = "Kode Unik Verifikasi", fontSize = 12.sp, color = TextSecondary)
                        Text(text = "+Rp $uniqueCode", fontSize = 12.sp, color = GsmGold)
                    }
                    if (couponDiscount > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Diskon Voucher", fontSize = 12.sp, color = GsmGreen)
                            Text(text = "-${GsmViewModel.formatRupiah(couponDiscount)}", fontSize = 12.sp, color = GsmGreen)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = GsmBorder)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Total Pembayaran", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        Text(
                            text = GsmViewModel.formatRupiah(finalTotal),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = GsmCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    if (name.isBlank() || whatsapp.isBlank()) {
                        return@Button
                    }
                    onSubmitOrder(name, whatsapp, email, selectedPayment, customInfo, notes)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("submit_checkout_btn"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GsmCyan,
                    contentColor = GsmBackground
                )
            ) {
                Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Bayar Sekarang (${GsmViewModel.formatRupiah(finalTotal)})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun defaultTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = GsmCyan,
    unfocusedBorderColor = GsmBorder,
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    focusedLabelColor = GsmCyan,
    unfocusedLabelColor = TextMuted
)
