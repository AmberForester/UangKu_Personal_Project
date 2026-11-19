package com.example.uangku.feature.analysis.presentation.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.feature.analysis.domain.MonthlyTrendPoint
import java.time.YearMonth
import kotlin.math.abs

@Composable
fun CategoryTrendLineChart(
    modifier: Modifier = Modifier,
    monthlyHistory: List<MonthlyTrendPoint>,
    averageAmount: Double,
    lineColor: Color,
    selectedMonth: YearMonth,
    selectedHistoryMonths: Int = 6,
    isLoading: Boolean = false,
    onRangeSelected: (Int) -> Unit = {},

) {
    if (monthlyHistory.isEmpty()) return

    // Titik yang sedang di-tap pengguna untuk melihat detail nominal (default: bulan target)
    var selectedPointIndex by remember(monthlyHistory, selectedMonth) {
        val idx = monthlyHistory.indexOfFirst { it.month == selectedMonth }
        mutableStateOf(if (idx != -1) idx else monthlyHistory.lastIndex)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing),
        label = "line_chart_anim"
    )

    val hasAnyData = monthlyHistory.any { it.amount > 0 }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Judul & Toggle Filter Rentang (3 Bulan | 6 Bulan | 1 Tahun)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tren Transaksi",
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Ketuk titik untuk melihat nominal",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.width(8.dp))

                // Segmented Toggle Rentang Waktu (3 Bulan, 6 Bulan, 1 Tahun)
                val ranges = listOf(
                    3 to "3 Bln",
                    6 to "6 Bln",
                    12 to "1 Thn"
                )

                Row(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    ranges.forEach { (months, label) ->
                        val isSelected = selectedHistoryMonths == months
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                            shadowElevation = if (isSelected) 1.dp else 0.dp,
                            modifier = Modifier.clickable(enabled = !isLoading) { onRangeSelected(months) }
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Sub-header: Legend Garis Rata-rata & Nilai Titik Terpilih
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (averageAmount > 0) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .width(16.dp)
                                .height(2.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    shape = RoundedCornerShape(1.dp)
                                )
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Rata-rata: ${currencyMinimalFormatter(averageAmount)}",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Spacer(Modifier.width(1.dp))
                }

                val activePoint = monthlyHistory.getOrNull(selectedPointIndex)
                if (activePoint != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = lineColor.copy(alpha = 0.12f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${activePoint.label}: ",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = lineColor,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currencyMinimalFormatter(activePoint.amount),
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(14.dp))

            // Visualisasi Line Chart Canvas dengan Isolated Loading
            val maxAmount = (monthlyHistory.maxOfOrNull { it.amount } ?: 0.0).coerceAtLeast(averageAmount)
            // Tambahkan headroom 25% agar titik tertinggi tidak menempel di paling atas
            val chartCeiling = if (maxAmount > 0) maxAmount * 1.25 else 100000.0

            val outlineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                if (!hasAnyData) {
                    val rangeName = if (selectedHistoryMonths == 12) "1 tahun" else "$selectedHistoryMonths bulan"
                    // Empty state ketika belum ada data sama sekali di rentang bulan ini
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Belum ada riwayat transaksi pada $rangeName terakhir",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .pointerInput(monthlyHistory) {
                                detectTapGestures { offset ->
                                    val count = monthlyHistory.size
                                    if (count <= 1) return@detectTapGestures
                                    val padX = if (count > 6) 16.dp.toPx() else 24.dp.toPx()
                                    val usableW = size.width - (padX * 2)
                                    val stepX = usableW / (count - 1)

                                    var closestIdx = 0
                                    var minDiff = Float.MAX_VALUE
                                    for (i in 0 until count) {
                                        val pointX = padX + (i * stepX)
                                        val diff = abs(offset.x - pointX)
                                        if (diff < minDiff) {
                                            minDiff = diff
                                            closestIdx = i
                                        }
                                    }
                                    selectedPointIndex = closestIdx
                                }
                            }
                    ) {
                        val count = monthlyHistory.size
                        if (count < 2) return@Canvas

                        val padX = if (count > 6) 16.dp.toPx() else 24.dp.toPx()
                        val padTop = 16.dp.toPx()
                        val padBottom = 16.dp.toPx()
                        val usableW = size.width - (padX * 2)
                        val usableH = size.height - padTop - padBottom
                        val stepX = usableW / (count - 1)

                        // 1. Gambar Garis Putus-putus Rata-rata (Dashed Average Line)
                        if (averageAmount > 0) {
                            val avgY = padTop + (usableH * (1f - (averageAmount / chartCeiling).toFloat()))
                            drawLine(
                                color = outlineColor,
                                start = Offset(padX, avgY),
                                end = Offset(size.width - padX, avgY),
                                strokeWidth = 1.5.dp.toPx(),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                            )
                        }

                        // Menghitung koordinat titik (X, Y)
                        val points = monthlyHistory.mapIndexed { index, point ->
                            val px = padX + (index * stepX)
                            val fraction = (point.amount / chartCeiling).toFloat().coerceIn(0f, 1f)
                            val py = padTop + (usableH * (1f - (fraction * animatedProgress)))
                            Offset(px, py)
                        }

                        // 2. Buat Path untuk Area Gradien di bawah kurva
                        val fillPath = Path().apply {
                            moveTo(points.first().x, size.height - padBottom)
                            lineTo(points.first().x, points.first().y)

                            for (i in 0 until points.size - 1) {
                                val p0 = points[i]
                                val p1 = points[i + 1]
                                val midX = (p0.x + p1.x) / 2f
                                cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
                            }

                            lineTo(points.last().x, size.height - padBottom)
                            close()
                        }

                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    lineColor.copy(alpha = 0.32f),
                                    lineColor.copy(alpha = 0.0f)
                                ),
                                startY = padTop,
                                endY = size.height - padBottom
                            )
                        )

                        // 3. Buat Path untuk Garis Kurva Utama
                        val strokePath = Path().apply {
                            moveTo(points.first().x, points.first().y)
                            for (i in 0 until points.size - 1) {
                                val p0 = points[i]
                                val p1 = points[i + 1]
                                val midX = (p0.x + p1.x) / 2f
                                cubicTo(midX, p0.y, midX, p1.y, p1.x, p1.y)
                            }
                        }

                        drawPath(
                            path = strokePath,
                            color = lineColor,
                            style = Stroke(
                                width = if (count > 6) 2.5.dp.toPx() else 3.dp.toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )

                        // 4. Gambar Titik-titik Data (Data Points)
                        points.forEachIndexed { index, point ->
                            val isSelected = index == selectedPointIndex
                            val isTargetMonth = monthlyHistory[index].month == selectedMonth

                            val haloRadius = if (count > 6) 8.dp.toPx() else 11.dp.toPx()
                            val outerRadius = if (isSelected) 5.dp.toPx() else if (count > 6) 3.5.dp.toPx() else 4.5.dp.toPx()
                            val innerRadius = if (isSelected) 2.5.dp.toPx() else if (count > 6) 1.5.dp.toPx() else 2.dp.toPx()

                            if (isSelected) {
                                // Cincin luar bercahaya untuk titik aktif
                                drawCircle(
                                    color = lineColor.copy(alpha = 0.22f),
                                    radius = haloRadius,
                                    center = point
                                )
                            }

                            // Lingkaran luar solid
                            drawCircle(
                                color = if (isSelected || isTargetMonth) lineColor else lineColor.copy(alpha = 0.8f),
                                radius = outerRadius,
                                center = point
                            )

                            // Titik putih di tengah
                            drawCircle(
                                color = Color.White,
                                radius = innerRadius,
                                center = point
                            )
                        }
                    }
                }

                // Isolated Subtle Loading Indicator on the chart area
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 2.5.dp,
                            color = lineColor
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Label Sumbu X (Bulan)
            val count = monthlyHistory.size
            val labelPadding = if (count > 6) 6.dp else 14.dp
            val fontSize = if (count > 6) 9.sp else 11.sp

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = labelPadding),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                monthlyHistory.forEachIndexed { index, point ->
                    val isSelected = index == selectedPointIndex
                    val isTargetMonth = point.month == selectedMonth

                    Text(
                        text = point.label,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = fontSize),
                        fontWeight = if (isSelected || isTargetMonth) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected || isTargetMonth) lineColor else MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
