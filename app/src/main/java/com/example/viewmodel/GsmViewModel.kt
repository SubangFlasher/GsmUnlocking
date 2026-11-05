package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SampleData
import com.example.model.CartItem
import com.example.model.OrderRecord
import com.example.model.OrderStatus
import com.example.model.PaymentChannel
import com.example.model.PriceRangeFilter
import com.example.model.PriceSortOrder
import com.example.model.Product
import com.example.model.ProductType
import com.example.model.ProductVariant
import com.example.model.RemoteExecutionStatus
import com.example.model.RemoteProgressStep
import com.example.model.RemoteTrackerData
import com.example.model.ServerStatusItem
import com.example.model.TechnicianStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class GsmUiState(
    val selectedTab: Int = 0, // 0 = Aktivasi, 1 = Sewa, 2 = Remote
    val searchQuery: String = "",
    val selectedCategory: String = "Semua",
    val selectedSortOrder: PriceSortOrder = PriceSortOrder.DEFAULT,
    val selectedPriceRange: PriceRangeFilter = PriceRangeFilter.PRESETS[0],
    val customMinPrice: Long? = null,
    val customMaxPrice: Long? = null,
    val isFilterSheetOpen: Boolean = false,
    val activeRemoteTrackerOrder: OrderRecord? = null,
    val cart: List<CartItem> = emptyList(),
    val orders: List<OrderRecord> = emptyList(),
    val activeProductDetail: Product? = null,
    val isCartOpen: Boolean = false,
    val isCheckoutOpen: Boolean = false,
    val checkoutTargetProduct: Product? = null,
    val checkoutTargetVariant: ProductVariant? = null,
    val activePaymentOrder: OrderRecord? = null,
    val activeInvoiceOrder: OrderRecord? = null,
    val isServerStatusOpen: Boolean = false,
    val isGuideOpen: Boolean = false,
    val isHistoryOpen: Boolean = false,
    val appliedCoupon: String = "",
    val couponDiscount: Long = 0L,
    val aktivasiMatches: Int = SampleData.aktivasiProducts.size,
    val sewaMatches: Int = SampleData.sewaProducts.size,
    val remoteMatches: Int = SampleData.remoteProducts.size,
    val serverStatuses: List<ServerStatusItem> = SampleData.serverStatuses,
    val technicians: List<TechnicianStatus> = SampleData.technicians,
    val reviews: List<com.example.model.ReviewItem> = SampleData.initialReviews,
    val isWriteReviewOpen: Boolean = false,
    val reviewTargetProduct: Product? = null
)

class GsmViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("gsm_unlocking_id_prefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(GsmUiState())
    val uiState: StateFlow<GsmUiState> = _uiState.asStateFlow()

    init {
        loadSavedOrders()
    }

    fun selectTab(tabIndex: Int) {
        _uiState.update { it.copy(selectedTab = tabIndex, selectedCategory = "Semua") }
    }

    private fun matchesProduct(prod: Product, query: String): Boolean {
        if (query.isBlank()) return true
        return prod.name.contains(query, ignoreCase = true) ||
                prod.subtitle.contains(query, ignoreCase = true) ||
                prod.description.contains(query, ignoreCase = true) ||
                prod.category.contains(query, ignoreCase = true) ||
                prod.supportedBrands.any { it.contains(query, ignoreCase = true) }
    }

    fun setSearchQuery(query: String) {
        val aktCount = SampleData.aktivasiProducts.count { matchesProduct(it, query) }
        val sewaCount = SampleData.sewaProducts.count { matchesProduct(it, query) }
        val remCount = SampleData.remoteProducts.count { matchesProduct(it, query) }
        _uiState.update {
            it.copy(
                searchQuery = query,
                aktivasiMatches = aktCount,
                sewaMatches = sewaCount,
                remoteMatches = remCount
            )
        }
    }

    fun clearSearch() {
        setSearchQuery("")
    }

    fun selectCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setSortOrder(order: PriceSortOrder) {
        _uiState.update { it.copy(selectedSortOrder = order) }
    }

    fun setPriceRange(range: PriceRangeFilter) {
        _uiState.update {
            it.copy(
                selectedPriceRange = range,
                customMinPrice = null,
                customMaxPrice = null
            )
        }
    }

    fun setCustomPriceRange(min: Long?, max: Long?) {
        val label = if (min != null && max != null) {
            "${formatRupiah(min)} - ${formatRupiah(max)}"
        } else if (min != null) {
            ">= ${formatRupiah(min)}"
        } else if (max != null) {
            "<= ${formatRupiah(max)}"
        } else {
            "Semua Budget"
        }

        _uiState.update {
            it.copy(
                selectedPriceRange = PriceRangeFilter("custom", label, min ?: 0L, max ?: Long.MAX_VALUE),
                customMinPrice = min,
                customMaxPrice = max
            )
        }
    }

    fun resetPriceFilter() {
        _uiState.update {
            it.copy(
                selectedSortOrder = PriceSortOrder.DEFAULT,
                selectedPriceRange = PriceRangeFilter.PRESETS[0],
                customMinPrice = null,
                customMaxPrice = null,
                selectedCategory = "Semua"
            )
        }
    }

    fun openFilterSheet() {
        _uiState.update { it.copy(isFilterSheetOpen = true) }
    }

    fun closeFilterSheet() {
        _uiState.update { it.copy(isFilterSheetOpen = false) }
    }

    fun filterAndSortProducts(products: List<Product>): List<Product> {
        val state = _uiState.value
        val minPrice = state.selectedPriceRange.minPrice
        val maxPrice = state.selectedPriceRange.maxPrice

        val filtered = products.filter { prod ->
            val matchesCategory = state.selectedCategory == "Semua" || prod.category.equals(state.selectedCategory, ignoreCase = true)
            val matchesSearch = matchesProduct(prod, state.searchQuery)
            val startingPrice = prod.variants.minOfOrNull { it.price } ?: 0L
            val matchesPrice = startingPrice in minPrice..maxPrice
            matchesCategory && matchesSearch && matchesPrice
        }

        return when (state.selectedSortOrder) {
            PriceSortOrder.DEFAULT -> filtered
            PriceSortOrder.PRICE_LOW_TO_HIGH -> filtered.sortedBy { prod -> prod.variants.minOfOrNull { it.price } ?: 0L }
            PriceSortOrder.PRICE_HIGH_TO_LOW -> filtered.sortedByDescending { prod -> prod.variants.minOfOrNull { it.price } ?: 0L }
            PriceSortOrder.NAME_ASC -> filtered.sortedBy { it.name }
        }
    }

    fun openProductDetail(product: Product) {
        _uiState.update { it.copy(activeProductDetail = product) }
    }

    fun closeProductDetail() {
        _uiState.update { it.copy(activeProductDetail = null) }
    }

    fun openWriteReview(product: Product? = null) {
        _uiState.update { it.copy(isWriteReviewOpen = true, reviewTargetProduct = product ?: it.activeProductDetail) }
    }

    fun closeWriteReview() {
        _uiState.update { it.copy(isWriteReviewOpen = false, reviewTargetProduct = null) }
    }

    fun submitReview(productId: String, authorName: String, rating: Int, comment: String, deviceOrVariant: String, roleBadge: String = "Teknisi Terverifikasi") {
        val newReview = com.example.model.ReviewItem(
            id = "rev_${System.currentTimeMillis()}",
            productId = productId,
            authorName = if (authorName.isBlank()) "Teknisi Mitra" else authorName,
            roleBadge = roleBadge,
            rating = rating.coerceIn(1, 5),
            comment = comment,
            dateString = "Baru saja",
            deviceOrVariantUsed = deviceOrVariant,
            isHelpfulCount = 0
        )
        _uiState.update { current ->
            current.copy(
                reviews = listOf(newReview) + current.reviews,
                isWriteReviewOpen = false,
                reviewTargetProduct = null
            )
        }
        Toast.makeText(getApplication(), "Ulasan & rating bintang $rating berhasil dikirim!", Toast.LENGTH_SHORT).show()
    }

    fun voteReviewHelpful(reviewId: String) {
        _uiState.update { current ->
            current.copy(
                reviews = current.reviews.map { rev ->
                    if (rev.id == reviewId) rev.copy(isHelpfulCount = rev.isHelpfulCount + 1) else rev
                }
            )
        }
        Toast.makeText(getApplication(), "Terimakasih atas feedback Anda!", Toast.LENGTH_SHORT).show()
    }

    fun addToCart(product: Product, variant: ProductVariant, customInfo: String = "") {
        val newItem = CartItem(
            id = UUID.randomUUID().toString(),
            product = product,
            selectedVariant = variant,
            customInfo = customInfo,
            quantity = 1
        )
        _uiState.update { current ->
            current.copy(cart = current.cart + newItem)
        }
        Toast.makeText(getApplication(), "${product.name} (${variant.name}) ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
    }

    fun removeFromCart(itemId: String) {
        _uiState.update { current ->
            current.copy(cart = current.cart.filter { it.id != itemId })
        }
    }

    fun openCart() {
        _uiState.update { it.copy(isCartOpen = true) }
    }

    fun closeCart() {
        _uiState.update { it.copy(isCartOpen = false) }
    }

    fun openCheckout(product: Product? = null, variant: ProductVariant? = null) {
        _uiState.update {
            it.copy(
                isCheckoutOpen = true,
                checkoutTargetProduct = product,
                checkoutTargetVariant = variant ?: product?.variants?.firstOrNull()
            )
        }
    }

    fun closeCheckout() {
        _uiState.update {
            it.copy(
                isCheckoutOpen = false,
                checkoutTargetProduct = null,
                checkoutTargetVariant = null,
                appliedCoupon = "",
                couponDiscount = 0L
            )
        }
    }

    fun applyCoupon(code: String) {
        val trimmed = code.trim().uppercase()
        if (trimmed == "GSMINDOPROMO" || trimmed == "TEKNISIBARU") {
            _uiState.update {
                it.copy(
                    appliedCoupon = trimmed,
                    couponDiscount = 25000L
                )
            }
            Toast.makeText(getApplication(), "Voucher diskon Rp 25.000 berhasil diterapkan!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(getApplication(), "Kode voucher tidak valid. Gunakan: GSMINDOPROMO", Toast.LENGTH_LONG).show()
        }
    }

    fun placeOrder(
        customerName: String,
        customerWhatsApp: String,
        customerEmail: String,
        paymentChannel: PaymentChannel,
        customInfoText: String,
        notes: String
    ) {
        val current = _uiState.value
        val items: List<CartItem> = if (current.checkoutTargetProduct != null && current.checkoutTargetVariant != null) {
            listOf(
                CartItem(
                    id = UUID.randomUUID().toString(),
                    product = current.checkoutTargetProduct,
                    selectedVariant = current.checkoutTargetVariant,
                    customInfo = customInfoText,
                    quantity = 1
                )
            )
        } else {
            current.cart
        }

        if (items.isEmpty()) {
            Toast.makeText(getApplication(), "Tidak ada produk yang dipesan", Toast.LENGTH_SHORT).show()
            return
        }

        val subtotal = items.sumOf { it.selectedVariant.price * it.quantity }
        val uniqueCode = (100..899).random()
        val discount = current.couponDiscount
        val total = (subtotal - discount + uniqueCode).coerceAtLeast(1000L)

        // Generate realistic credentials / access keys based on product type
        val generatedCredentials = generateCredentials(items)

        val isRemoteService = items.any { it.product.type == ProductType.REMOTE }
        val orderId = "GSM-" + (100000..999999).random()
        val initialTracker = if (isRemoteService) {
            val remoteItem = items.first { it.product.type == ProductType.REMOTE }
            createInitialRemoteTracker(orderId, remoteItem.product.name, customInfoText)
        } else null

        val newOrder = OrderRecord(
            id = orderId,
            createdAt = System.currentTimeMillis(),
            items = items,
            customerName = customerName,
            customerWhatsApp = customerWhatsApp,
            customerEmail = customerEmail,
            paymentChannel = paymentChannel,
            subtotal = subtotal,
            uniqueCode = uniqueCode,
            discount = discount,
            total = total,
            status = OrderStatus.MENUNGGU_PEMBAYARAN,
            deliveredCredentials = generatedCredentials,
            notes = notes,
            remoteTrackerData = initialTracker
        )

        // Save order and clear cart if ordering from cart
        val updatedOrders = listOf(newOrder) + current.orders
        _uiState.update {
            it.copy(
                orders = updatedOrders,
                cart = if (current.checkoutTargetProduct == null) emptyList() else current.cart,
                isCheckoutOpen = false,
                checkoutTargetProduct = null,
                checkoutTargetVariant = null,
                isCartOpen = false,
                activePaymentOrder = newOrder
            )
        }
        saveOrders(updatedOrders)
    }

    private fun generateCredentials(items: List<CartItem>): String {
        val item = items.firstOrNull() ?: return ""
        val prod = item.product
        val variant = item.selectedVariant

        return when (prod.type) {
            ProductType.AKTIVASI -> {
                """
                === DETAIL LISENSI RESMI GSM UNLOCKING INDONESIA ===
                Produk: ${prod.name} (${variant.name})
                Status Aktivasi: AKTIF RESMI DI SERVER
                License Key / Serial: GSM-ACT-${UUID.randomUUID().toString().take(12).uppercase()}
                Akun Terdaftar: ${item.customInfo.ifBlank { "Sesuai Username Pemesan" }}
                Masa Berlaku: ${variant.name} (Garansi Server Penuh)
                """.trimIndent()
            }
            ProductType.SEWA -> {
                val userNumber = (10..99).random()
                """
                === KREDENSIAL LOGIN SEWA GSM UNLOCKING ID ===
                Software: ${prod.name}
                Durasi Sewa: ${variant.name}
                Username Server: GSM_VIP_SLOT$userNumber
                Password Server: unlocker#${(1000..9999).random()}
                Aturan Sewa:
                1. Dilarang logout atau ganti PC sebelum durasi habis.
                2. Jangan ubah password server.
                3. Klik logout di software jika pekerjaan telah selesai.
                """.trimIndent()
            }
            ProductType.REMOTE -> {
                """
                === TIKET REMOTE SERVICE TEKNISI MASTER ===
                Layanan: ${prod.name}
                Teknisi Bertugas: Bang Budi & Kang Asep (ONLINE)
                Antrean Servis: #02 (Estimasi 5-10 menit dimulai)
                Koneksi: UltraViewer / AnyDesk
                Info Unit HP: ${item.customInfo.ifBlank { "Unit terlampir" }}
                Instruksi:
                1. Buka software UltraViewer / AnyDesk di PC Anda.
                2. Hubungkan HP via kabel data USB original.
                3. Hubungi CS WhatsApp untuk konfirmasi remote dimulai.
                """.trimIndent()
            }
        }
    }

    fun confirmPayment(orderId: String) {
        viewModelScope.launch {
            val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            // Update status to DIPROSES & In Progress for remote
            _uiState.update { current ->
                val updated = current.orders.map { order ->
                    if (order.id == orderId) {
                        val updatedTracker = order.remoteTrackerData?.let { trk ->
                            trk.copy(
                                status = RemoteExecutionStatus.IN_PROGRESS,
                                progressPercentage = 0.60f,
                                estimatedTimeRemaining = "4 - 8 Menit",
                                steps = trk.steps.mapIndexed { idx, s ->
                                    when (idx) {
                                        0 -> s.copy(isCompleted = true, isCurrent = false)
                                        1 -> s.copy(isCompleted = true, isCurrent = false, timestamp = timeStr)
                                        2 -> s.copy(isCompleted = false, isCurrent = true, timestamp = timeStr)
                                        else -> s
                                    }
                                },
                                logs = trk.logs + "[$timeStr] Pembayaran terverifikasi. Sesi remote terhubung dengan Master Teknisi."
                            )
                        }
                        order.copy(status = OrderStatus.DIPROSES_SERVER, remoteTrackerData = updatedTracker)
                    } else order
                }
                current.copy(orders = updated, activePaymentOrder = null)
            }
            Toast.makeText(getApplication(), "Pembayaran diterima! Teknisi & server sedang memproses...", Toast.LENGTH_SHORT).show()

            // Auto transition to SELESAI / COMPLETED after 3.5 seconds
            delay(3500)
            val finishTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            _uiState.update { current ->
                val updated = current.orders.map { order ->
                    if (order.id == orderId) {
                        val completedTracker = order.remoteTrackerData?.let { trk ->
                            trk.copy(
                                status = RemoteExecutionStatus.COMPLETED,
                                progressPercentage = 1.0f,
                                estimatedTimeRemaining = "Selesai",
                                steps = trk.steps.map { it.copy(isCompleted = true, isCurrent = false) },
                                logs = trk.logs + listOf(
                                    "[$finishTime] Handshake & auth server berhasil.",
                                    "[$finishTime] Eksekusi bypass / flashing berhasil 100%.",
                                    "[$finishTime] Sesi remote selesai. Unit smartphone normal kembali."
                                )
                            )
                        }
                        order.copy(status = OrderStatus.SELESAI_AKTIF, remoteTrackerData = completedTracker)
                    } else order
                }
                current.copy(orders = updated)
            }
            val completedOrder = _uiState.value.orders.find { it.id == orderId }
            if (completedOrder != null) {
                _uiState.update { it.copy(activeInvoiceOrder = completedOrder) }
            }
            saveOrders(_uiState.value.orders)
        }
    }

    fun openRemoteTracker(order: OrderRecord) {
        _uiState.update { it.copy(activeRemoteTrackerOrder = order) }
    }

    fun closeRemoteTracker() {
        _uiState.update { it.copy(activeRemoteTrackerOrder = null) }
    }

    fun advanceRemoteStatus(orderId: String) {
        val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        _uiState.update { current ->
            val updated = current.orders.map { order ->
                if (order.id == orderId && order.remoteTrackerData != null) {
                    val tracker = order.remoteTrackerData
                    val nextTracker = when (tracker.status) {
                        RemoteExecutionStatus.PENDING -> {
                            tracker.copy(
                                status = RemoteExecutionStatus.IN_PROGRESS,
                                progressPercentage = 0.65f,
                                estimatedTimeRemaining = "3 - 5 Menit",
                                steps = tracker.steps.mapIndexed { idx, s ->
                                    when (idx) {
                                        0 -> s.copy(isCompleted = true, isCurrent = false)
                                        1 -> s.copy(isCompleted = true, isCurrent = false, timestamp = timeStr)
                                        2 -> s.copy(isCompleted = true, isCurrent = false, timestamp = timeStr)
                                        3 -> s.copy(isCompleted = false, isCurrent = true, timestamp = timeStr)
                                        else -> s
                                    }
                                },
                                logs = tracker.logs + listOf(
                                    "[$timeStr] Teknisi Bang Budi terhubung via UltraViewer.",
                                    "[$timeStr] Handshake server OK. Eksekusi software dimulai."
                                )
                            )
                        }
                        RemoteExecutionStatus.IN_PROGRESS -> {
                            tracker.copy(
                                status = RemoteExecutionStatus.COMPLETED,
                                progressPercentage = 1.0f,
                                estimatedTimeRemaining = "Selesai",
                                steps = tracker.steps.map { it.copy(isCompleted = true, isCurrent = false) },
                                logs = tracker.logs + listOf(
                                    "[$timeStr] Eksekusi bypass sukses! Device reboot normal.",
                                    "[$timeStr] Pekerjaan selesai dan terverifikasi 100% normal."
                                )
                            )
                        }
                        RemoteExecutionStatus.COMPLETED -> {
                            tracker.copy(
                                status = RemoteExecutionStatus.PENDING,
                                progressPercentage = 0.20f,
                                estimatedTimeRemaining = "10 - 15 Menit",
                                steps = tracker.steps.mapIndexed { idx, s ->
                                    when (idx) {
                                        0 -> s.copy(isCompleted = true, isCurrent = false)
                                        1 -> s.copy(isCompleted = false, isCurrent = true)
                                        else -> s.copy(isCompleted = false, isCurrent = false)
                                    }
                                },
                                logs = listOf(
                                    "[$timeStr] Status reset ke antrean baru untuk pengujian demo.",
                                    "[$timeStr] Menunggu teknisi mengambil sesi remote..."
                                )
                            )
                        }
                    }
                    order.copy(remoteTrackerData = nextTracker)
                } else order
            }
            val activeTracker = updated.find { it.id == current.activeRemoteTrackerOrder?.id }
            current.copy(orders = updated, activeRemoteTrackerOrder = activeTracker)
        }
        val target = _uiState.value.orders.find { it.id == orderId }
        val newStatus = target?.remoteTrackerData?.status?.label ?: ""
        Toast.makeText(getApplication(), "Status Remote: $newStatus", Toast.LENGTH_SHORT).show()
    }

    fun createDemoRemoteOrder() {
        val orderId = "GSM-REM-" + (1000..9999).random()
        val prod = SampleData.remoteProducts[0]
        val tracker = createInitialRemoteTracker(
            orderId = orderId,
            serviceName = prod.name,
            customInfo = "Xiaomi Redmi Note 12 - UltraViewer ID 44 819 201 pass: 9912"
        )
        val demoOrder = OrderRecord(
            id = orderId,
            createdAt = System.currentTimeMillis(),
            items = listOf(
                CartItem(
                    id = "demo_$orderId",
                    product = prod,
                    selectedVariant = prod.variants[0],
                    customInfo = "Xiaomi Redmi Note 12 - UltraViewer ID 44 819 201",
                    quantity = 1
                )
            ),
            customerName = "Teknisi Baru (Demo)",
            customerWhatsApp = "081298765432",
            customerEmail = "teknisi@gsm.id",
            paymentChannel = PaymentChannel.QRIS,
            subtotal = prod.variants[0].price,
            uniqueCode = 104,
            discount = 0L,
            total = prod.variants[0].price + 104L,
            status = OrderStatus.SELESAI_AKTIF,
            deliveredCredentials = "Tiket Remote Aktif",
            remoteTrackerData = tracker
        )
        _uiState.update { it.copy(orders = listOf(demoOrder) + it.orders) }
        Toast.makeText(getApplication(), "Order remote demo berhasil dibuat! Status: Pending", Toast.LENGTH_SHORT).show()
    }

    fun createInitialRemoteTracker(orderId: String, serviceName: String, customInfo: String): RemoteTrackerData {
        val now = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        val steps = listOf(
            RemoteProgressStep("Tiket Antrean Diterbitkan", "Permintaan remote tercatat di server", now, isCompleted = true, isCurrent = false),
            RemoteProgressStep("Menunggu Teknisi Terhubung", "Teknisi sedang menyiapkan session UltraViewer/AnyDesk", now, isCompleted = false, isCurrent = true),
            RemoteProgressStep("Inisialisasi Handshake Server", "Koneksi kabel data & verifikasi token auth", "-", isCompleted = false, isCurrent = false),
            RemoteProgressStep("Eksekusi Bypass & Repair", "Proses unlock/flash/repair berlangsung", "-", isCompleted = false, isCurrent = false),
            RemoteProgressStep("Verifikasi Akhir & Unit Normal", "Pengecekan fungsi HP selesai", "-", isCompleted = false, isCurrent = false)
        )
        val logs = listOf(
            "[$now] Tiket antrean remote #$orderId diterbitkan.",
            "[$now] Unit: ${customInfo.ifBlank { "Detail HP terlampir" }}",
            "[$now] Status: PENDING (Menunggu giliran eksekusi oleh Master Teknisi)."
        )
        return RemoteTrackerData(
            orderId = orderId,
            serviceName = serviceName,
            deviceModel = customInfo.ifBlank { "Smartphone Android / iOS" },
            remoteSoftware = if (customInfo.contains("AnyDesk", true)) "AnyDesk" else "UltraViewer",
            remoteId = customInfo.take(24),
            assignedTechnician = "Bang Budi (Master Unlocker)",
            status = RemoteExecutionStatus.PENDING,
            progressPercentage = 0.20f,
            estimatedTimeRemaining = "10 - 15 Menit",
            steps = steps,
            logs = logs
        )
    }

    fun openPaymentOrder(order: OrderRecord) {
        _uiState.update { it.copy(activePaymentOrder = order) }
    }

    fun closePaymentOrder() {
        _uiState.update { it.copy(activePaymentOrder = null) }
    }

    fun openInvoice(order: OrderRecord) {
        _uiState.update { it.copy(activeInvoiceOrder = order) }
    }

    fun closeInvoice() {
        _uiState.update { it.copy(activeInvoiceOrder = null) }
    }

    fun openServerStatus() {
        _uiState.update { it.copy(isServerStatusOpen = true) }
    }

    fun closeServerStatus() {
        _uiState.update { it.copy(isServerStatusOpen = false) }
    }

    fun openGuide() {
        _uiState.update { it.copy(isGuideOpen = true) }
    }

    fun closeGuide() {
        _uiState.update { it.copy(isGuideOpen = false) }
    }

    fun openHistory() {
        _uiState.update { it.copy(isHistoryOpen = true) }
    }

    fun closeHistory() {
        _uiState.update { it.copy(isHistoryOpen = false) }
    }

    private fun saveOrders(orders: List<OrderRecord>) {
        val editor = prefs.edit()
        editor.putInt("order_count", orders.size)
        orders.forEachIndexed { index, order ->
            editor.putString("order_${index}_id", order.id)
            editor.putString("order_${index}_name", order.customerName)
            editor.putString("order_${index}_wa", order.customerWhatsApp)
            editor.putLong("order_${index}_total", order.total)
            editor.putString("order_${index}_status", order.status.name)
            editor.putString("order_${index}_creds", order.deliveredCredentials ?: "")
            editor.putString("order_${index}_title", order.items.firstOrNull()?.product?.name ?: "")
            editor.putString("order_${index}_variant", order.items.firstOrNull()?.selectedVariant?.name ?: "")
        }
        editor.apply()
    }

    private fun loadSavedOrders() {
        val count = prefs.getInt("order_count", 0)
        if (count == 0) {
            // Seed 1 example active transaction so user sees instant state
            val seedOrder = OrderRecord(
                id = "GSM-928174",
                createdAt = System.currentTimeMillis() - 3600000L * 2,
                items = listOf(
                    CartItem(
                        id = "seed_item_1",
                        product = SampleData.aktivasiProducts[0],
                        selectedVariant = SampleData.aktivasiProducts[0].variants[1],
                        customInfo = "teknisi_pro_id / arifin@gsm.id",
                        quantity = 1
                    )
                ),
                customerName = "Arifin GSM Servis",
                customerWhatsApp = "081298765432",
                customerEmail = "arifin@gsm.id",
                paymentChannel = PaymentChannel.QRIS,
                subtotal = 495000L,
                uniqueCode = 248,
                discount = 25000L,
                total = 470248L,
                status = OrderStatus.SELESAI_AKTIF,
                deliveredCredentials = """
                === LISENSI RESMI UNLOCKTOOL AKTIF ===
                Serial Key: UT-ACT-9821-4820-ID
                Username: teknisi_pro_id
                Status: AKTIF (6 BULAN)
                Garansi Server: Resmi GSM UNLOCKING INDONESIA
                """.trimIndent()
            )
            val seedRemoteOrder = OrderRecord(
                id = "GSM-REM-7819",
                createdAt = System.currentTimeMillis() - 720000L,
                items = listOf(
                    CartItem(
                        id = "remote_seed_1",
                        product = SampleData.remoteProducts[0],
                        selectedVariant = SampleData.remoteProducts[0].variants[0],
                        customInfo = "Samsung Galaxy A54 5G - UltraViewer ID 28 991 201 pass: 8821",
                        quantity = 1
                    )
                ),
                customerName = "Arifin GSM Servis",
                customerWhatsApp = "081298765432",
                customerEmail = "arifin@gsm.id",
                paymentChannel = PaymentChannel.QRIS,
                subtotal = 85000L,
                uniqueCode = 112,
                discount = 0L,
                total = 85112L,
                status = OrderStatus.SELESAI_AKTIF,
                deliveredCredentials = "Tiket Remote Aktif - Teknisi Terhubung",
                remoteTrackerData = RemoteTrackerData(
                    orderId = "GSM-REM-7819",
                    serviceName = "Remote Bypass FRP Samsung All Series",
                    deviceModel = "Samsung Galaxy A54 5G (Android 14)",
                    remoteSoftware = "UltraViewer",
                    remoteId = "28 991 201",
                    assignedTechnician = "Bang Budi (Master Unlocker)",
                    status = RemoteExecutionStatus.IN_PROGRESS,
                    progressPercentage = 0.65f,
                    estimatedTimeRemaining = "4 Menit",
                    steps = listOf(
                        RemoteProgressStep("Tiket Antrean Diterbitkan", "Permintaan terverifikasi server", "10:00", isCompleted = true, isCurrent = false),
                        RemoteProgressStep("Teknisi Terhubung", "UltraViewer ID terhubung stabil", "10:04", isCompleted = true, isCurrent = false),
                        RemoteProgressStep("Inisialisasi Handshake Server", "Samsung Knox Guard server auth OK", "10:06", isCompleted = true, isCurrent = false),
                        RemoteProgressStep("Eksekusi Bypass & Repair", "Sedang menghapus FRP lock via MTP mode...", "10:08", isCompleted = false, isCurrent = true),
                        RemoteProgressStep("Verifikasi Akhir & Unit Normal", "Menunggu reboot HP selesai", "-", isCompleted = false, isCurrent = false)
                    ),
                    logs = listOf(
                        "[10:00:12] Tiket remote GSM-REM-7819 diterbitkan.",
                        "[10:04:30] Teknisi Bang Budi terhubung ke UltraViewer client.",
                        "[10:06:15] Driver Samsung Mobile USB terdeteksi (COM14).",
                        "[10:07:02] Knox Guard server status: GRANTED. Mengirim paket bypass...",
                        "[10:08:40] Proses bypass FRP sedang berlangsung (65%)."
                    )
                )
            )
            val seedCompletedRemoteOrder = OrderRecord(
                id = "GSM-REM-6420",
                createdAt = System.currentTimeMillis() - 86400000L * 2, // 2 days ago
                items = listOf(
                    CartItem(
                        id = "remote_seed_completed_1",
                        product = SampleData.remoteProducts[1], // Mi Cloud Clean / Bypass
                        selectedVariant = SampleData.remoteProducts[1].variants[0],
                        customInfo = "Xiaomi Redmi Note 12 Pro - Lock Clean ID 88 123 999",
                        quantity = 1
                    )
                ),
                customerName = "Arifin GSM Servis",
                customerWhatsApp = "081298765432",
                customerEmail = "arifin@gsm.id",
                paymentChannel = PaymentChannel.BCA,
                subtotal = 120000L,
                uniqueCode = 540,
                discount = 0L,
                total = 120540L,
                status = OrderStatus.SELESAI_AKTIF,
                deliveredCredentials = "SUKSES: Mi Cloud Clean ID terverifikasi server Xiaomi. Garansi remote aktif 7 hari.",
                remoteTrackerData = RemoteTrackerData(
                    orderId = "GSM-REM-6420",
                    serviceName = "Remote Bypass Mi Cloud Clean / Tamper",
                    deviceModel = "Xiaomi Redmi Note 12 Pro 5G",
                    remoteSoftware = "UltraViewer",
                    remoteId = "88 123 999",
                    assignedTechnician = "Kang Asep (Xiaomi & EDL Specialist)",
                    status = RemoteExecutionStatus.COMPLETED,
                    progressPercentage = 1.0f,
                    estimatedTimeRemaining = "Selesai (0 Menit)",
                    steps = listOf(
                        RemoteProgressStep("Tiket Antrean Diterbitkan", "Permintaan terverifikasi server", "14:10", isCompleted = true, isCurrent = false),
                        RemoteProgressStep("Teknisi Terhubung", "UltraViewer ID terhubung stabil", "14:15", isCompleted = true, isCurrent = false),
                        RemoteProgressStep("Inisialisasi Handshake Server", "EDL 9008 Auth Server Xiaomi OK", "14:18", isCompleted = true, isCurrent = false),
                        RemoteProgressStep("Eksekusi Bypass & Repair", "Mi Account storage cleared successfully", "14:24", isCompleted = true, isCurrent = false),
                        RemoteProgressStep("Verifikasi Akhir & Unit Normal", "Unit booting normal ke MIUI Home. Selesai!", "14:28", isCompleted = true, isCurrent = true)
                    ),
                    logs = listOf(
                        "[14:10:02] Tiket remote GSM-REM-6420 diterbitkan.",
                        "[14:15:20] Teknisi Kang Asep terhubung ke UltraViewer.",
                        "[14:18:45] Qualcomm HS-USB QDLoader 9008 (COM7) OK.",
                        "[14:21:10] Xiaomi Auth Token Server: APPROVED.",
                        "[14:24:00] Flashing Mi Cloud clean partition... Success.",
                        "[14:28:15] Rebooting device... Normal status. Service COMPLETED."
                    )
                )
            )

            val seedSewaOrder = OrderRecord(
                id = "GSM-SEW-3105",
                createdAt = System.currentTimeMillis() - 86400000L * 4, // 4 days ago
                items = listOf(
                    CartItem(
                        id = "sewa_seed_1",
                        product = SampleData.sewaProducts[0], // Sewa UnlockTool
                        selectedVariant = SampleData.sewaProducts[0].variants[0], // 2 Jam
                        customInfo = "Teknisi Arifin / Slot Pagi",
                        quantity = 1
                    )
                ),
                customerName = "Arifin GSM Servis",
                customerWhatsApp = "081298765432",
                customerEmail = "arifin@gsm.id",
                paymentChannel = PaymentChannel.QRIS,
                subtotal = 15000L,
                uniqueCode = 21,
                discount = 0L,
                total = 15021L,
                status = OrderStatus.SELESAI_AKTIF,
                deliveredCredentials = """
                === AKUN SEWA UNLOCKTOOL (2 JAM) ===
                User ID: UT-RENT-082
                Password: gsm-unlock-2026
                Login Server: SG-Server-01
                Masa Aktif: Selesai digunakan
                """.trimIndent()
            )

            _uiState.update { it.copy(orders = listOf(seedRemoteOrder, seedOrder, seedCompletedRemoteOrder, seedSewaOrder)) }
            return
        }

        val loaded = mutableListOf<OrderRecord>()
        for (i in 0 until count) {
            val id = prefs.getString("order_${i}_id", "") ?: ""
            if (id.isBlank()) continue
            val name = prefs.getString("order_${i}_name", "Teknisi") ?: "Teknisi"
            val wa = prefs.getString("order_${i}_wa", "08123456789") ?: "08123456789"
            val total = prefs.getLong("order_${i}_total", 50000L)
            val statusName = prefs.getString("order_${i}_status", OrderStatus.SELESAI_AKTIF.name) ?: OrderStatus.SELESAI_AKTIF.name
            val status = try { OrderStatus.valueOf(statusName) } catch (_: Exception) { OrderStatus.SELESAI_AKTIF }
            val creds = prefs.getString("order_${i}_creds", "")
            val title = prefs.getString("order_${i}_title", "UnlockTool License") ?: "UnlockTool License"
            val variantName = prefs.getString("order_${i}_variant", "6 Bulan") ?: "6 Bulan"

            val item = CartItem(
                id = "item_$id",
                product = SampleData.aktivasiProducts[0].copy(name = title),
                selectedVariant = ProductVariant("v_$id", variantName, total),
                quantity = 1
            )

            loaded.add(
                OrderRecord(
                    id = id,
                    items = listOf(item),
                    customerName = name,
                    customerWhatsApp = wa,
                    customerEmail = "$name@teknisi.id",
                    paymentChannel = PaymentChannel.QRIS,
                    subtotal = total,
                    uniqueCode = 123,
                    discount = 0L,
                    total = total,
                    status = status,
                    deliveredCredentials = creds
                )
            )
        }
        if (loaded.isNotEmpty()) {
            _uiState.update { it.copy(orders = loaded) }
        }
    }

    companion object {
        fun formatRupiah(amount: Long): String {
            val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
            format.maximumFractionDigits = 0
            return format.format(amount).replace("Rp", "Rp ")
        }
    }
}
