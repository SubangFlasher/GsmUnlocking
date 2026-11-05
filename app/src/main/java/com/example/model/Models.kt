package com.example.model

enum class ProductType(val title: String) {
    AKTIVASI("Aktivasi"),
    SEWA("Sewa"),
    REMOTE("Remote")
}

data class ProductVariant(
    val id: String,
    val name: String, // e.g. "3 Bulan", "6 Bulan", "12 Bulan" or "2 Jam", "6 Jam", "12 Jam"
    val price: Long,
    val originalPrice: Long? = null,
    val note: String = ""
)

data class Product(
    val id: String,
    val name: String,
    val subtitle: String,
    val description: String,
    val type: ProductType,
    val category: String, // e.g. "Multi-Tool", "Samsung", "Xiaomi", "Hardware Schematics", "Dongle"
    val variants: List<ProductVariant>,
    val features: List<String>,
    val supportedBrands: List<String>,
    val badge: String? = null, // e.g. "TERLARIS", "INSTANT", "SLOT TERSEDIA"
    val isAvailable: Boolean = true,
    val availableSlots: Int = 5,
    val processTime: String = "5 - 15 Menit",
    val requiresAccountField: String = "Username & Email Terdaftar", // For Aktivasi: username tool, For Remote: Model & Remote ID
    val rating: Float = 4.9f,
    val reviewCount: Int = 128
)

data class ReviewItem(
    val id: String,
    val productId: String,
    val authorName: String,
    val roleBadge: String = "Teknisi Terverifikasi", // "Teknisi Counter", "Master Flasher", "Pelanggan Terverifikasi"
    val rating: Int, // 1 to 5
    val comment: String,
    val dateString: String,
    val deviceOrVariantUsed: String = "",
    val isHelpfulCount: Int = 0
)

data class CartItem(
    val id: String,
    val product: Product,
    val selectedVariant: ProductVariant,
    val customInfo: String = "", // e.g. username tool or IMEI
    val quantity: Int = 1
)

enum class OrderStatus(val label: String) {
    MENUNGGU_PEMBAYARAN("Menunggu Pembayaran"),
    DIPROSES_SERVER("Diproses Server"),
    SELESAI_AKTIF("Aktif / Selesai"),
    BATAL("Dibatalkan")
}

enum class PaymentChannel(val code: String, val displayName: String, val accountNumber: String, val accountHolder: String) {
    QRIS("QRIS", "QRIS Instant (Semua Bank & E-Wallet)", "ID1020083921021", "GSM UNLOCKING INDONESIA"),
    BCA("BCA", "Bank BCA Virtual Account", "8277 0812 3456 7890", "GSM UNLOCK INDONESIA"),
    MANDIRI("MANDIRI", "Bank Mandiri VA", "8890 0812 3456 7890", "GSM UNLOCK INDONESIA"),
    DANA("DANA", "DANA Bisnis", "0812-3456-7890", "GSM UNLOCKING ID"),
    GOPAY("GOPAY", "GoPay / GoPay Later", "0812-3456-7890", "GSM UNLOCKING ID")
}

enum class RemoteExecutionStatus(val label: String, val description: String) {
    PENDING("Pending", "Menunggu antrean teknisi & standby UltraViewer"),
    IN_PROGRESS("In Progress", "Sedang dikerjakan via remote software"),
    COMPLETED("Completed", "Proses sukses & perangkat kembali normal")
}

data class RemoteProgressStep(
    val title: String,
    val description: String,
    val timestamp: String,
    val isCompleted: Boolean,
    val isCurrent: Boolean
)

data class RemoteTrackerData(
    val orderId: String,
    val serviceName: String,
    val deviceModel: String,
    val remoteSoftware: String = "UltraViewer",
    val remoteId: String = "",
    val assignedTechnician: String = "Bang Budi (Master Unlocker)",
    val status: RemoteExecutionStatus = RemoteExecutionStatus.PENDING,
    val progressPercentage: Float = 0.25f,
    val estimatedTimeRemaining: String = "10 - 15 Menit",
    val steps: List<RemoteProgressStep> = emptyList(),
    val logs: List<String> = emptyList()
)

data class OrderRecord(
    val id: String,
    val createdAt: Long = System.currentTimeMillis(),
    val items: List<CartItem>,
    val customerName: String,
    val customerWhatsApp: String,
    val customerEmail: String,
    val paymentChannel: PaymentChannel,
    val subtotal: Long,
    val uniqueCode: Int,
    val discount: Long = 0L,
    val total: Long,
    val status: OrderStatus,
    val deliveredCredentials: String? = null, // Username/password/license key or Remote status
    val notes: String = "",
    val remoteTrackerData: RemoteTrackerData? = null
)

data class ServerStatusItem(
    val name: String,
    val category: String,
    val status: String, // "ONLINE", "NORMAL", "SLOW", "MAINTENANCE"
    val ping: Int, // ms
    val lastUpdate: String
)

data class TechnicianStatus(
    val name: String,
    val role: String,
    val isOnline: Boolean,
    val rating: String,
    val handledCount: Int
)

enum class PriceSortOrder(val label: String, val shortLabel: String) {
    DEFAULT("Rekomendasi", "Default"),
    PRICE_LOW_TO_HIGH("Harga: Termurah ke Termahal", "Termurah"),
    PRICE_HIGH_TO_LOW("Harga: Termahal ke Termurah", "Termahal"),
    NAME_ASC("Nama: A - Z", "A - Z")
}

data class PriceRangeFilter(
    val id: String,
    val label: String,
    val minPrice: Long = 0L,
    val maxPrice: Long = Long.MAX_VALUE
) {
    companion object {
        val PRESETS = listOf(
            PriceRangeFilter("all", "Semua Budget", 0L, Long.MAX_VALUE),
            PriceRangeFilter("under_50k", "< Rp 50rb", 0L, 50000L),
            PriceRangeFilter("50k_200k", "Rp 50rb - 200rb", 50000L, 200000L),
            PriceRangeFilter("200k_500k", "Rp 200rb - 500rb", 200000L, 500000L),
            PriceRangeFilter("above_500k", "> Rp 500rb", 500000L, Long.MAX_VALUE)
        )
    }
}

