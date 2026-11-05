package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderRecord
import com.example.model.RemoteExecutionStatus
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
fun RemoteTrackerCard(
    remoteOrder: OrderRecord?,
    onOpenTracker: (OrderRecord) -> Unit,
    onAdvanceStatus: (String) -> Unit,
    onCreateDemo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Pulsing animation for real-time indicator
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    if (remoteOrder == null || remoteOrder.remoteTrackerData == null) {
        // Empty state with demo trigger
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, GsmBorder, RoundedCornerShape(16.dp))
                .testTag("remote_tracker_empty_card"),
            colors = CardDefaults.cardColors(containerColor = GsmSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Sensors, contentDescription = null, tint = GsmCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "STATUS TRACKER REAL-TIME",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = GsmCyan,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(GsmSurfaceVariant)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = "STANDBY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Pantau status pengerjaan servis remote Anda secara langsung (Pending • In Progress • Completed).",
                    fontSize = 12.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onCreateDemo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("demo_tracker_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = GsmSurfaceElevated),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = GsmCyan, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Coba Buat Order Remote Demo", fontSize = 11.sp, color = GsmCyan, fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    val tracker = remoteOrder.remoteTrackerData

    val statusColor = when (tracker.status) {
        RemoteExecutionStatus.PENDING -> GsmGold
        RemoteExecutionStatus.IN_PROGRESS -> GsmCyan
        RemoteExecutionStatus.COMPLETED -> GsmGreen
    }

    val animatedProgress by animateFloatAsState(
        targetValue = tracker.progressPercentage,
        animationSpec = tween(500),
        label = "progress"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.5.dp,
                brush = Brush.horizontalGradient(listOf(statusColor.copy(alpha = 0.8f), GsmBorder)),
                shape = RoundedCornerShape(16.dp)
            )
            .testTag("remote_tracker_card"),
        colors = CardDefaults.cardColors(containerColor = GsmSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Pulsing dot
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(statusColor.copy(alpha = pulseAlpha))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "REAL-TIME TRACKER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = statusColor,
                        letterSpacing = 1.sp
                    )
                }

                // Status Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .border(1.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = tracker.status.label.uppercase(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = statusColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Service & Device info
            Text(
                text = tracker.serviceName,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "ID: ${tracker.orderId}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GsmCyan,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "• ${tracker.deviceModel}",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3-Stage Visual Stepper (Pending -> In Progress -> Completed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(GsmSurfaceElevated)
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusStepPill(
                    title = "Pending",
                    isActive = tracker.status == RemoteExecutionStatus.PENDING,
                    isDone = tracker.status == RemoteExecutionStatus.IN_PROGRESS || tracker.status == RemoteExecutionStatus.COMPLETED,
                    activeColor = GsmGold
                )
                Text(text = "→", fontSize = 11.sp, color = TextMuted)
                StatusStepPill(
                    title = "In Progress",
                    isActive = tracker.status == RemoteExecutionStatus.IN_PROGRESS,
                    isDone = tracker.status == RemoteExecutionStatus.COMPLETED,
                    activeColor = GsmCyan
                )
                Text(text = "→", fontSize = 11.sp, color = TextMuted)
                StatusStepPill(
                    title = "Completed",
                    isActive = tracker.status == RemoteExecutionStatus.COMPLETED,
                    isDone = tracker.status == RemoteExecutionStatus.COMPLETED,
                    activeColor = GsmGreen
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar & ETA
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Progres: ${(tracker.progressPercentage * 100).toInt()}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Estimasi: ${tracker.estimatedTimeRemaining}",
                    fontSize = 11.sp,
                    color = statusColor,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = statusColor,
                trackColor = GsmSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Latest log preview
            val latestLog = tracker.logs.lastOrNull() ?: "Menunggu koneksi sesi remote..."
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(GsmBackground)
                    .border(0.5.dp, GsmBorder, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = ">",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = latestLog,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        fontFamily = FontFamily.Monospace,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { onOpenTracker(remoteOrder) },
                    modifier = Modifier
                        .weight(1.1f)
                        .height(38.dp)
                        .testTag("view_tracker_detail_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    shape = RoundedCornerShape(10.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = Brush.horizontalGradient(listOf(GsmBorder, GsmBorder))
                    )
                ) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Detail Log", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onAdvanceStatus(remoteOrder.id) },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(38.dp)
                        .testTag("advance_remote_status_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor, contentColor = GsmBackground),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when (tracker.status) {
                            RemoteExecutionStatus.PENDING -> "Mulai Remote"
                            RemoteExecutionStatus.IN_PROGRESS -> "Selesaikan"
                            RemoteExecutionStatus.COMPLETED -> "Uji Ulang"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusStepPill(
    title: String,
    isActive: Boolean,
    isDone: Boolean,
    activeColor: Color
) {
    val pillBg = when {
        isActive -> activeColor.copy(alpha = 0.2f)
        isDone -> GsmGreen.copy(alpha = 0.15f)
        else -> Color.Transparent
    }

    val pillTextColor = when {
        isActive -> activeColor
        isDone -> GsmGreen
        else -> TextMuted
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(pillBg)
            .border(
                width = if (isActive) 1.dp else 0.dp,
                color = if (isActive) activeColor else Color.Transparent,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (isDone && !isActive) {
                Text(text = "✓", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = GsmGreen)
                Spacer(modifier = Modifier.width(3.dp))
            }
            Text(
                text = title,
                fontSize = 10.sp,
                fontWeight = if (isActive || isDone) FontWeight.Bold else FontWeight.Medium,
                color = pillTextColor
            )
        }
    }
}
