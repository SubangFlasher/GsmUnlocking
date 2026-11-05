package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
fun RemoteTrackerDialog(
    order: OrderRecord,
    onDismiss: () -> Unit,
    onAdvanceStatus: (String) -> Unit
) {
    val context = LocalContext.current
    val tracker = order.remoteTrackerData ?: return

    val statusColor = when (tracker.status) {
        RemoteExecutionStatus.PENDING -> GsmGold
        RemoteExecutionStatus.IN_PROGRESS -> GsmCyan
        RemoteExecutionStatus.COMPLETED -> GsmGreen
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, statusColor.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                .testTag("remote_tracker_dialog"),
            colors = CardDefaults.cardColors(containerColor = GsmSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Sensors, contentDescription = null, tint = statusColor, modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Live Status Tracker Remote",
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

                // Status Banner Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusColor.copy(alpha = 0.12f))
                        .border(1.dp, statusColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "STATUS PENGERJAAN:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                                Text(
                                    text = tracker.status.label,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Black,
                                    color = statusColor
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GsmSurfaceElevated)
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${(tracker.progressPercentage * 100).toInt()}% Selesai",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = statusColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = { tracker.progressPercentage },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = statusColor,
                            trackColor = GsmSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Estimasi Selesai:", fontSize = 11.sp, color = TextMuted)
                            Text(text = tracker.estimatedTimeRemaining, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Service Details & Technician
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(GsmSurfaceElevated)
                        .padding(12.dp)
                ) {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "ID Order", fontSize = 11.sp, color = TextMuted)
                            Text(text = tracker.orderId, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GsmCyan)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Layanan", fontSize = 11.sp, color = TextMuted)
                            Text(text = tracker.serviceName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Unit HP", fontSize = 11.sp, color = TextMuted)
                            Text(text = tracker.deviceModel, fontSize = 11.sp, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Software", fontSize = 11.sp, color = TextMuted)
                            Text(text = tracker.remoteSoftware, fontSize = 11.sp, color = TextPrimary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Teknisi On-Duty", fontSize = 11.sp, color = TextMuted)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(GsmGreen))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = tracker.assignedTechnician, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GsmGreen)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Step-by-Step Vertical Timeline
                Text(
                    text = "TAHAPAN PENGERJAAN REMOT:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMuted,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    tracker.steps.forEachIndexed { index, step ->
                        val stepColor = when {
                            step.isCompleted -> GsmGreen
                            step.isCurrent -> statusColor
                            else -> TextMuted
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (step.isCurrent) statusColor.copy(alpha = 0.1f) else GsmSurfaceVariant.copy(alpha = 0.5f))
                                .border(
                                    width = if (step.isCurrent) 1.dp else 0.5.dp,
                                    color = if (step.isCurrent) statusColor else GsmBorder,
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(stepColor.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                if (step.isCompleted) {
                                    Text(text = "✓", fontSize = 12.sp, fontWeight = FontWeight.Black, color = GsmGreen)
                                } else {
                                    Text(text = "${index + 1}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = stepColor)
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = step.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (step.isCurrent) FontWeight.Bold else FontWeight.Medium,
                                    color = if (step.isCurrent) TextPrimary else TextSecondary
                                )
                                Text(text = step.description, fontSize = 10.sp, color = TextMuted)
                            }

                            if (step.timestamp != "-") {
                                Text(text = step.timestamp, fontSize = 10.sp, color = TextMuted, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Live Terminal Log Box
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Terminal, contentDescription = null, tint = statusColor, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "LIVE SYSTEM LOGS:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(GsmBackground)
                        .border(1.dp, GsmBorder, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        tracker.logs.forEach { logLine ->
                            Text(
                                text = logLine,
                                fontSize = 10.sp,
                                color = if (logLine.contains("selesai", true) || logLine.contains("sukses", true)) GsmGreen else TextSecondary,
                                fontFamily = FontFamily.Monospace,
                                lineHeight = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Interactive Status Advancer & WhatsApp Chat
                Button(
                    onClick = { onAdvanceStatus(order.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("simulate_advance_status_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = statusColor, contentColor = GsmBackground),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = when (tracker.status) {
                            RemoteExecutionStatus.PENDING -> "Uji Pindah ke 'In Progress'"
                            RemoteExecutionStatus.IN_PROGRESS -> "Uji Pindah ke 'Completed'"
                            RemoteExecutionStatus.COMPLETED -> "Uji Reset ke 'Pending'"
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        val message = "Halo Teknisi ${tracker.assignedTechnician},\n" +
                                "Saya ingin menanyakan progress remote service:\n" +
                                "ID Order: ${tracker.orderId}\n" +
                                "Layanan: ${tracker.serviceName}\n" +
                                "Status: ${tracker.status.label}"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/6281234567890?text=${Uri.encode(message)}"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("whatsapp_technician_btn"),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Chat, contentDescription = null, tint = GsmGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Chat Langsung Teknisi On-Duty", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
