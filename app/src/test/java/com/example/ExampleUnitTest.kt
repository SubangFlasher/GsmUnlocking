package com.example

import com.example.data.SampleData
import com.example.model.PaymentChannel
import com.example.model.ProductType
import com.example.viewmodel.GsmViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun verifySampleProductsCategorizedAcross3Menus() {
        val aktivasi = SampleData.aktivasiProducts
        val sewa = SampleData.sewaProducts
        val remote = SampleData.remoteProducts

        assertTrue("Aktivasi products must not be empty", aktivasi.isNotEmpty())
        assertTrue("Sewa products must not be empty", sewa.isNotEmpty())
        assertTrue("Remote products must not be empty", remote.isNotEmpty())

        assertTrue(aktivasi.all { it.type == ProductType.AKTIVASI })
        assertTrue(sewa.all { it.type == ProductType.SEWA })
        assertTrue(remote.all { it.type == ProductType.REMOTE })
    }

    @Test
    fun verifyRupiahFormatting() {
        assertEquals("Rp 325.000", GsmViewModel.formatRupiah(325000L))
        assertEquals("Rp 1.150.000", GsmViewModel.formatRupiah(1150000L))
        assertEquals("Rp 20.000", GsmViewModel.formatRupiah(20000L))
    }

    @Test
    fun verifyProductVariantsPricing() {
        val unlocktool = SampleData.aktivasiProducts.first { it.id == "akt_unlocktool" }
        assertEquals(3, unlocktool.variants.size)
        assertEquals("3 Bulan", unlocktool.variants[0].name)
        assertEquals(325000L, unlocktool.variants[0].price)
        assertEquals("6 Bulan", unlocktool.variants[1].name)
        assertEquals(495000L, unlocktool.variants[1].price)
        assertEquals("12 Bulan (1 Tahun)", unlocktool.variants[2].name)
        assertEquals(740000L, unlocktool.variants[2].price)
    }

    @Test
    fun verifyPaymentChannelsConfigured() {
        val channels = PaymentChannel.values()
        assertTrue(channels.contains(PaymentChannel.QRIS))
        assertTrue(channels.contains(PaymentChannel.BCA))
        assertTrue(channels.contains(PaymentChannel.MANDIRI))
        assertTrue(channels.contains(PaymentChannel.DANA))
        assertTrue(channels.contains(PaymentChannel.GOPAY))
    }

    @Test
    fun verifySearchFilteringAcrossCategories() {
        val query = "UnlockTool"
        val aktMatches = SampleData.aktivasiProducts.filter {
            it.name.contains(query, true) || it.subtitle.contains(query, true)
        }
        val sewaMatches = SampleData.sewaProducts.filter {
            it.name.contains(query, true) || it.subtitle.contains(query, true)
        }
        assertTrue("UnlockTool should be found in Aktivasi", aktMatches.isNotEmpty())
        assertTrue("UnlockTool should be found in Sewa", sewaMatches.isNotEmpty())

        val remoteQuery = "FRP"
        val remoteMatches = SampleData.remoteProducts.filter {
            it.name.contains(remoteQuery, true) || it.description.contains(remoteQuery, true)
        }
        assertTrue("FRP should be found in Remote services", remoteMatches.isNotEmpty())
    }

    @Test
    fun verifyPriceRangeFiltering() {
        val allSewa = SampleData.sewaProducts
        // Filter under 50k
        val under50k = allSewa.filter { (it.variants.minOfOrNull { v -> v.price } ?: 0L) <= 50000L }
        assertTrue("Under 50k should find affordable rental tools like UnlockTool 2h or Borneo 1d", under50k.isNotEmpty())

        // Filter premium activation over 500k
        val allAkt = SampleData.aktivasiProducts
        val above500k = allAkt.filter { (it.variants.minOfOrNull { v -> v.price } ?: 0L) >= 500000L }
        assertTrue("Above 500k should find 1-year licenses like Chimera or Pandora", above500k.isNotEmpty())
    }

    @Test
    fun verifyPriceSorting() {
        val allSewa = SampleData.sewaProducts
        val sortedAsc = allSewa.sortedBy { it.variants.minOfOrNull { v -> v.price } ?: 0L }
        val firstPrice = sortedAsc.first().variants.minOf { it.price }
        val lastPrice = sortedAsc.last().variants.minOf { it.price }
        assertTrue("Cheapest rental must be <= highest rental", firstPrice <= lastPrice)

        val sortedDesc = allSewa.sortedByDescending { it.variants.minOfOrNull { v -> v.price } ?: 0L }
        val descFirst = sortedDesc.first().variants.minOf { it.price }
        val descLast = sortedDesc.last().variants.minOf { it.price }
        assertTrue("Highest rental must be >= lowest rental", descFirst >= descLast)
    }

    @Test
    fun verifyRemoteStatusTrackerTransitions() {
        // Test RemoteExecutionStatus values
        val statuses = com.example.model.RemoteExecutionStatus.values()
        assertEquals(3, statuses.size)
        assertEquals(com.example.model.RemoteExecutionStatus.PENDING, statuses[0])
        assertEquals(com.example.model.RemoteExecutionStatus.IN_PROGRESS, statuses[1])
        assertEquals(com.example.model.RemoteExecutionStatus.COMPLETED, statuses[2])

        // Verify Status Labels and Badges
        assertEquals("Pending", com.example.model.RemoteExecutionStatus.PENDING.label)
        assertEquals("In Progress", com.example.model.RemoteExecutionStatus.IN_PROGRESS.label)
        assertEquals("Completed", com.example.model.RemoteExecutionStatus.COMPLETED.label)

        // Verify status description
        assertTrue(com.example.model.RemoteExecutionStatus.PENDING.description.contains("antrean", ignoreCase = true))
        assertTrue(com.example.model.RemoteExecutionStatus.IN_PROGRESS.description.contains("dikerjakan", ignoreCase = true))
        assertTrue(com.example.model.RemoteExecutionStatus.COMPLETED.description.contains("sukses", ignoreCase = true))
    }

    @Test
    fun verifyOrderHistoryCategorizationAndRemoteServices() {
        val remoteOrder = com.example.model.OrderRecord(
            id = "GSM-REM-TEST",
            createdAt = System.currentTimeMillis(),
            items = listOf(
                com.example.model.CartItem(
                    id = "c1",
                    product = SampleData.remoteProducts[0],
                    selectedVariant = SampleData.remoteProducts[0].variants[0]
                )
            ),
            customerName = "Tester",
            customerWhatsApp = "0812345678",
            customerEmail = "test@gsm.id",
            paymentChannel = com.example.model.PaymentChannel.QRIS,
            subtotal = 85000L,
            uniqueCode = 10,
            discount = 0L,
            total = 85010L,
            status = com.example.model.OrderStatus.SELESAI_AKTIF,
            remoteTrackerData = com.example.model.RemoteTrackerData(
                orderId = "GSM-REM-TEST",
                serviceName = "Remote Bypass",
                deviceModel = "Samsung Galaxy S23",
                status = com.example.model.RemoteExecutionStatus.COMPLETED,
                progressPercentage = 1.0f,
                steps = emptyList(),
                logs = emptyList()
            )
        )

        val licenseOrder = com.example.model.OrderRecord(
            id = "GSM-AKT-TEST",
            createdAt = System.currentTimeMillis() - 10000L,
            items = listOf(
                com.example.model.CartItem(
                    id = "c2",
                    product = SampleData.aktivasiProducts[0],
                    selectedVariant = SampleData.aktivasiProducts[0].variants[0]
                )
            ),
            customerName = "Tester",
            customerWhatsApp = "0812345678",
            customerEmail = "test@gsm.id",
            paymentChannel = com.example.model.PaymentChannel.BCA,
            subtotal = 325000L,
            uniqueCode = 12,
            discount = 0L,
            total = 325012L,
            status = com.example.model.OrderStatus.SELESAI_AKTIF
        )

        val allOrders = listOf(remoteOrder, licenseOrder)

        // Filter: Remote Services
        val remoteList = allOrders.filter { it.items.any { item -> item.product.type == com.example.model.ProductType.REMOTE } || it.remoteTrackerData != null }
        assertEquals(1, remoteList.size)
        assertEquals("GSM-REM-TEST", remoteList[0].id)
        assertEquals(com.example.model.RemoteExecutionStatus.COMPLETED, remoteList[0].remoteTrackerData?.status)

        // Filter: License & Rental
        val licenseList = allOrders.filter { it.items.any { item -> item.product.type == com.example.model.ProductType.AKTIVASI || item.product.type == com.example.model.ProductType.SEWA } }
        assertEquals(1, licenseList.size)
        assertEquals("GSM-AKT-TEST", licenseList[0].id)
    }

    @Test
    fun verifyReviewAndRatingSystem() {
        val initialReviews = SampleData.initialReviews
        assertTrue("Initial reviews must not be empty", initialReviews.isNotEmpty())

        // Verify rating bounds (1 to 5 stars)
        assertTrue(initialReviews.all { it.rating in 1..5 })

        // Check UnlockTool reviews exist
        val utReviews = initialReviews.filter { it.productId == "akt_unlocktool" }
        assertTrue("UnlockTool should have technician reviews", utReviews.isNotEmpty())
        assertTrue("Reviews should have author names", utReviews.all { it.authorName.isNotBlank() })
        assertTrue("Reviews should have verified roles", utReviews.all { it.roleBadge.isNotBlank() })

        // Check remote service reviews exist
        val remoteReviews = initialReviews.filter { it.productId == "rem_samsung_frp" || it.productId == "rem_xiaomi_micloud" }
        assertTrue("Remote services must have feedback", remoteReviews.isNotEmpty())

        // Test creating a new review item
        val newReview = com.example.model.ReviewItem(
            id = "test_rev",
            productId = "akt_chimeratool",
            authorName = "Master Fathur",
            roleBadge = "Teknisi Terverifikasi",
            rating = 5,
            comment = "Mantap sangat memuaskan!",
            dateString = "Baru saja",
            deviceOrVariantUsed = "Samsung 1 Tahun",
            isHelpfulCount = 0
        )
        assertEquals(5, newReview.rating)
        val helpfulVoted = newReview.copy(isHelpfulCount = newReview.isHelpfulCount + 1)
        assertEquals(1, helpfulVoted.isHelpfulCount)
    }
}
