package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.data.SampleData
import com.example.ui.components.CartDialog
import com.example.ui.components.CheckoutBottomSheet
import com.example.ui.components.GsmBottomNavBar
import com.example.ui.components.GsmTopAppBar
import com.example.ui.components.GuideDialog
import com.example.ui.components.OrderDetailDialog
import com.example.ui.components.OrderHistoryDialog
import com.example.ui.components.PaymentQrisDialog
import com.example.ui.components.PriceFilterBottomSheet
import com.example.ui.components.ProductDetailBottomSheet
import com.example.ui.components.RemoteTrackerDialog
import com.example.ui.components.ServerStatusDialog
import com.example.ui.components.WriteReviewDialog
import com.example.ui.screens.AktivasiScreen
import com.example.ui.screens.RemoteScreen
import com.example.ui.screens.SewaScreen
import com.example.ui.theme.GsmBackground
import com.example.ui.theme.GsmCyan
import com.example.ui.theme.GsmGold
import com.example.ui.theme.GsmGreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GsmViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: GsmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                GsmApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun GsmApp(viewModel: GsmViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Handle system back button gracefully
    BackHandler(
        enabled = uiState.isWriteReviewOpen ||
                uiState.activeProductDetail != null ||
                uiState.isCheckoutOpen ||
                uiState.isFilterSheetOpen ||
                uiState.activePaymentOrder != null ||
                uiState.activeInvoiceOrder != null ||
                uiState.activeRemoteTrackerOrder != null ||
                uiState.isServerStatusOpen ||
                uiState.isGuideOpen ||
                uiState.isCartOpen ||
                uiState.isHistoryOpen ||
                uiState.selectedTab != 0
    ) {
        when {
            uiState.isWriteReviewOpen -> viewModel.closeWriteReview()
            uiState.searchQuery.isNotEmpty() -> viewModel.clearSearch()
            uiState.isFilterSheetOpen -> viewModel.closeFilterSheet()
            uiState.activeProductDetail != null -> viewModel.closeProductDetail()
            uiState.activeRemoteTrackerOrder != null -> viewModel.closeRemoteTracker()
            uiState.isCheckoutOpen -> viewModel.closeCheckout()
            uiState.activePaymentOrder != null -> viewModel.closePaymentOrder()
            uiState.activeInvoiceOrder != null -> viewModel.closeInvoice()
            uiState.isServerStatusOpen -> viewModel.closeServerStatus()
            uiState.isGuideOpen -> viewModel.closeGuide()
            uiState.isCartOpen -> viewModel.closeCart()
            uiState.isHistoryOpen -> viewModel.closeHistory()
            uiState.selectedTab != 0 -> viewModel.selectTab(0)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(GsmBackground),
        containerColor = GsmBackground,
        topBar = {
            GsmTopAppBar(
                searchQuery = uiState.searchQuery,
                onSearchChange = { viewModel.setSearchQuery(it) },
                onClearSearch = { viewModel.clearSearch() },
                selectedTab = uiState.selectedTab,
                onSelectTab = { viewModel.selectTab(it) },
                aktivasiCount = uiState.aktivasiMatches,
                sewaCount = uiState.sewaMatches,
                remoteCount = uiState.remoteMatches,
                cartCount = uiState.cart.size,
                ordersCount = uiState.orders.size,
                onOpenCart = { viewModel.openCart() },
                onOpenHistory = { viewModel.openHistory() },
                onOpenServerStatus = { viewModel.openServerStatus() },
                onOpenGuide = { viewModel.openGuide() }
            )
        },
        bottomBar = {
            GsmBottomNavBar(
                selectedTab = uiState.selectedTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.selectedTab) {
                0 -> {
                    AktivasiScreen(
                        products = SampleData.aktivasiProducts,
                        searchQuery = uiState.searchQuery,
                        selectedCategory = uiState.selectedCategory,
                        currentSort = uiState.selectedSortOrder,
                        currentRange = uiState.selectedPriceRange,
                        onCategoryChange = { viewModel.selectCategory(it) },
                        onSortChange = { viewModel.setSortOrder(it) },
                        onRangeChange = { viewModel.setPriceRange(it) },
                        onOpenFilterSheet = { viewModel.openFilterSheet() },
                        onResetFilters = { viewModel.resetPriceFilter() },
                        onDetailClick = { viewModel.openProductDetail(it) },
                        onOrderClick = { prod, variant -> viewModel.openCheckout(prod, variant) }
                    )
                }
                1 -> {
                    SewaScreen(
                        products = SampleData.sewaProducts,
                        searchQuery = uiState.searchQuery,
                        selectedCategory = uiState.selectedCategory,
                        currentSort = uiState.selectedSortOrder,
                        currentRange = uiState.selectedPriceRange,
                        onCategoryChange = { viewModel.selectCategory(it) },
                        onSortChange = { viewModel.setSortOrder(it) },
                        onRangeChange = { viewModel.setPriceRange(it) },
                        onOpenFilterSheet = { viewModel.openFilterSheet() },
                        onResetFilters = { viewModel.resetPriceFilter() },
                        onDetailClick = { viewModel.openProductDetail(it) },
                        onOrderClick = { prod, variant -> viewModel.openCheckout(prod, variant) }
                    )
                }
                2 -> {
                    RemoteScreen(
                        products = SampleData.remoteProducts,
                        remoteOrders = uiState.orders.filter { it.remoteTrackerData != null },
                        searchQuery = uiState.searchQuery,
                        selectedCategory = uiState.selectedCategory,
                        currentSort = uiState.selectedSortOrder,
                        currentRange = uiState.selectedPriceRange,
                        onCategoryChange = { viewModel.selectCategory(it) },
                        onSortChange = { viewModel.setSortOrder(it) },
                        onRangeChange = { viewModel.setPriceRange(it) },
                        onOpenFilterSheet = { viewModel.openFilterSheet() },
                        onResetFilters = { viewModel.resetPriceFilter() },
                        onDetailClick = { viewModel.openProductDetail(it) },
                        onOrderClick = { prod, variant -> viewModel.openCheckout(prod, variant) },
                        onOpenTracker = { order -> viewModel.openRemoteTracker(order) },
                        onAdvanceStatus = { orderId -> viewModel.advanceRemoteStatus(orderId) },
                        onCreateDemoTracker = { viewModel.createDemoRemoteOrder() }
                    )
                }
            }
        }
    }

    // Product Detail Sheet
    uiState.activeProductDetail?.let { product ->
        ProductDetailBottomSheet(
            product = product,
            reviews = uiState.reviews,
            onDismiss = { viewModel.closeProductDetail() },
            onAddToCart = { prod, variant, info ->
                viewModel.addToCart(prod, variant, info)
            },
            onDirectCheckout = { prod, variant ->
                viewModel.openCheckout(prod, variant)
            },
            onWriteReview = { targetProd ->
                viewModel.openWriteReview(targetProd)
            },
            onVoteHelpful = { reviewId ->
                viewModel.voteReviewHelpful(reviewId)
            }
        )
    }

    // Checkout Sheet
    if (uiState.isCheckoutOpen) {
        CheckoutBottomSheet(
            targetProduct = uiState.checkoutTargetProduct,
            targetVariant = uiState.checkoutTargetVariant,
            cartItems = uiState.cart,
            appliedCoupon = uiState.appliedCoupon,
            couponDiscount = uiState.couponDiscount,
            onApplyCoupon = { viewModel.applyCoupon(it) },
            onDismiss = { viewModel.closeCheckout() },
            onSubmitOrder = { name, wa, email, payment, customInfo, notes ->
                viewModel.placeOrder(name, wa, email, payment, customInfo, notes)
            }
        )
    }

    // Payment QRIS / VA Dialog
    uiState.activePaymentOrder?.let { order ->
        PaymentQrisDialog(
            order = order,
            onDismiss = { viewModel.closePaymentOrder() },
            onConfirmPayment = { orderId ->
                viewModel.confirmPayment(orderId)
            }
        )
    }

    // Invoice / Order Detail Dialog
    uiState.activeInvoiceOrder?.let { order ->
        OrderDetailDialog(
            order = order,
            onDismiss = { viewModel.closeInvoice() },
            onPayNow = { pendingOrder ->
                viewModel.closeInvoice()
                viewModel.openPaymentOrder(pendingOrder)
            },
            onOpenRemoteTracker = { remoteOrder ->
                viewModel.closeInvoice()
                viewModel.openRemoteTracker(remoteOrder)
            }
        )
    }

    // Server Status Dialog
    if (uiState.isServerStatusOpen) {
        ServerStatusDialog(
            serverStatuses = uiState.serverStatuses,
            technicians = uiState.technicians,
            onDismiss = { viewModel.closeServerStatus() }
        )
    }

    // Guide & Driver Dialog
    if (uiState.isGuideOpen) {
        GuideDialog(
            onDismiss = { viewModel.closeGuide() }
        )
    }

    // Cart Dialog
    if (uiState.isCartOpen) {
        CartDialog(
            cartItems = uiState.cart,
            onDismiss = { viewModel.closeCart() },
            onRemoveItem = { viewModel.removeFromCart(it) },
            onProceedCheckout = {
                viewModel.closeCart()
                viewModel.openCheckout()
            }
        )
    }

    // Order History Dialog
    if (uiState.isHistoryOpen) {
        OrderHistoryDialog(
            orders = uiState.orders,
            onDismiss = { viewModel.closeHistory() },
            onSelectOrder = { order ->
                viewModel.closeHistory()
                viewModel.openInvoice(order)
            },
            onOpenRemoteTracker = { remoteOrder ->
                viewModel.closeHistory()
                viewModel.openRemoteTracker(remoteOrder)
            }
        )
    }

    // Price Range & Sort Filter Sheet
    if (uiState.isFilterSheetOpen) {
        val accentColor = when (uiState.selectedTab) {
            0 -> GsmCyan
            1 -> GsmGold
            else -> GsmGreen
        }
        PriceFilterBottomSheet(
            currentSort = uiState.selectedSortOrder,
            currentRange = uiState.selectedPriceRange,
            customMinPrice = uiState.customMinPrice,
            customMaxPrice = uiState.customMaxPrice,
            accentColor = accentColor,
            onApply = { sort, range, min, max ->
                viewModel.setSortOrder(sort)
                if (range != null) {
                    viewModel.setPriceRange(range)
                } else {
                    viewModel.setCustomPriceRange(min, max)
                }
            },
            onReset = { viewModel.resetPriceFilter() },
            onDismiss = { viewModel.closeFilterSheet() }
        )
    }

    // Real-Time Remote Tracker Modal Dialog
    uiState.activeRemoteTrackerOrder?.let { order ->
        RemoteTrackerDialog(
            order = order,
            onDismiss = { viewModel.closeRemoteTracker() },
            onAdvanceStatus = { orderId -> viewModel.advanceRemoteStatus(orderId) }
        )
    }

    // Write Review & Rating Dialog
    if (uiState.isWriteReviewOpen) {
        val targetProduct = uiState.reviewTargetProduct ?: uiState.activeProductDetail ?: SampleData.aktivasiProducts.first()
        WriteReviewDialog(
            product = targetProduct,
            onDismiss = { viewModel.closeWriteReview() },
            onSubmit = { rating, author, role, comment, deviceOrVariant ->
                viewModel.submitReview(targetProduct.id, author, rating, comment, deviceOrVariant, role)
            }
        )
    }
}
