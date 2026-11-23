package com.example.uangku.feature.analysis.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.feature.analysis.domain.CategorySpending
import kotlin.math.atan2

val AnalysisChartPalette = listOf(
    Color(0xFF2563EB), // Vibrant Royal Blue
    Color(0xFFF97316), // Vivid Orange
    Color(0xFF10B981), // Emerald Green
    Color(0xFF8B5CF6), // Rich Purple
    Color(0xFFEC4899), // Pink
    Color(0xFFEAB308), // Amber Yellow
    Color(0xFF06B6D4), // Cyan
    Color(0xFF14B8A6), // Teal
    Color(0xFFF43F5E), // Rose Red
    Color(0xFF6366F1), // Indigo
)

fun getCategoryColor(index: Int): Color {
    return AnalysisChartPalette[index % AnalysisChartPalette.size]
}

@Composable
fun AnalysisDonutChart(
    modifier: Modifier = Modifier,
    totalAmount: Double,
    categorySpendings: List<CategorySpending>,
    selectedCategory: CategorySpending?,
    onCategoryClick: (CategorySpending?) -> Unit,
    selectedType: Type = Type.EXPENSE,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "donut_animation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 22.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(220.dp)
            ) {
                val placeholderTrackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)

                Canvas(
                    modifier = Modifier
                        .size(200.dp)
                        .pointerInput(categorySpendings, totalAmount) {
                            detectTapGestures { offset ->
                                if (totalAmount <= 0 || categorySpendings.isEmpty()) return@detectTapGestures

                                val center = Offset(size.width / 2f, size.height / 2f)
                                val touchX = offset.x - center.x
                                val touchY = offset.y - center.y

                                // Hitung sudut sentuhan dalam derajat (0 - 360) dimulai dari atas (-90 deg)
                                var angle = Math.toDegrees(atan2(touchY.toDouble(), touchX.toDouble())).toFloat()
                                if (angle < 0) angle += 360f

                                val normalizedAngle = (angle + 90f) % 360f

                                var cumulative = 0f
                                var clickedCategory: CategorySpending? = null

                                for (item in categorySpendings) {
                                    val sweep = item.percentage * 360f
                                    if (normalizedAngle >= cumulative && normalizedAngle <= cumulative + sweep) {
                                        clickedCategory = item
                                        break
                                    }
                                    cumulative += sweep
                                }

                                onCategoryClick(clickedCategory)
                            }
                        }
                ) {
                    val defaultStroke = 22.dp.toPx()
                    val selectedStroke = 28.dp.toPx()
                    val maxStroke = selectedStroke
                    val arcSize = Size(size.width - maxStroke, size.height - maxStroke)
                    val topLeft = Offset(maxStroke / 2f, maxStroke / 2f)

                    if (totalAmount <= 0 || categorySpendings.isEmpty()) {
                        drawArc(
                            color = placeholderTrackColor,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = defaultStroke)
                        )
                    } else {
                        var startAngle = -90f
                        val gapAngle = if (categorySpendings.size > 1) 3f else 0f

                        categorySpendings.forEachIndexed { index, item ->
                            val isSelected = selectedCategory?.categoryId == item.categoryId
                            val color = getCategoryColor(index)
                            val targetSweep = (item.percentage * 360f) * animatedProgress
                            val sweepWithGap = (targetSweep - gapAngle).coerceAtLeast(0.5f)

                            val sliceAlpha = if (selectedCategory == null) {
                                1f
                            } else if (isSelected) {
                                1f
                            } else {
                                0.22f
                            }

                            val currentStroke = if (isSelected) selectedStroke else defaultStroke

                            drawArc(
                                color = color.copy(alpha = sliceAlpha),
                                startAngle = startAngle + (gapAngle / 2f),
                                sweepAngle = sweepWithGap,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(
                                    width = currentStroke,
                                    cap = StrokeCap.Butt
                                )
                            )

                            startAngle += targetSweep
                        }
                    }
                }

                // Bagian Tengah Donut Chart dengan Transisi Halus (AnimatedContent)
                AnimatedContent(
                    targetState = selectedCategory,
                    transitionSpec = {
                        fadeIn(tween(220)) togetherWith fadeOut(tween(180))
                    },
                    label = "center_text_transition",
                    contentAlignment = Alignment.Center
                ) { targetCat ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 30.dp)
                    ) {
                        if (totalAmount <= 0 || categorySpendings.isEmpty()) {
                            val emptyText = if (selectedType == Type.EXPENSE) {
                                "Belum Ada Pengeluaran"
                            } else {
                                "Belum Ada Pemasukan"
                            }
                            Text(
                                text = emptyText,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        } else if (targetCat != null) {
                            Text(
                                text = targetCat.categoryName,
                                style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp),
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = currencyMinimalFormatter(targetCat.totalAmount),
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.5.sp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = "${(targetCat.percentage * 100).toInt()}% dari total",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp),
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            val totalLabel = if (selectedType == Type.EXPENSE) {
                                "Total Pengeluaran"
                            } else {
                                "Total Pemasukan"
                            }
                            Text(
                                text = totalLabel,
                                style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = currencyMinimalFormatter(totalAmount),
                                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.5.sp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(3.dp))
                            Text(
                                text = "${categorySpendings.size} Kategori",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp),
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
