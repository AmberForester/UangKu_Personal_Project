package com.example.uangku.feature.transaction.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.mainSkyBlue
import com.example.uangku.core.ui.theme.secondaryBgSkyBlue
import com.example.uangku.core.ui.theme.secondaryLightSkyBlue
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.category.domain.Category

@Composable
fun TransactionSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClearSearch: () -> Unit,
    selectedCategory: Category?,
    onOpenFilter: () -> Unit,
    onClearCategoryFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val hasFilter = selectedCategory != null

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Kolom Input Pencarian (Ramping 44dp, Vertically Centered, Tanpa Clipping)
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (searchQuery.isNotBlank()) mainSkyBlue else secondaryBgSkyBlue
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Cari",
                        tint = if (searchQuery.isNotBlank()) mainSkyBlue else Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Cari transaksi...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 13.5.sp,
                                    lineHeight = 18.sp
                                ),
                                color = Color(0xFF94A3B8),
                                maxLines = 1
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = onSearchQueryChange,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            cursorBrush = SolidColor(mainSkyBlue),
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                lineHeight = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF0F172A)
                            )
                        )
                    }

                    if (searchQuery.isNotBlank()) {
                        IconButton(
                            onClick = onClearSearch,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hapus teks",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.width(10.dp))

            // 2. Tombol Filter Kategori (Presisi 44dp selaras dengan kolom pencarian)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (hasFilter) mainSkyBlue else Color.White,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (hasFilter) mainSkyBlue else secondaryBgSkyBlue
                ),
                modifier = Modifier
                    .size(48.dp)
                    .clickable { onOpenFilter() },
                shadowElevation = if (hasFilter) 2.dp else 0.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.FilterAlt,
                        contentDescription = "Filter Kategori",
                        tint = if (hasFilter) Color.White else Color(0xFF475569),
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }

        // 3. Chip Indikator Kategori Aktif (jika ada kategori terpilih)
        AnimatedVisibility(
            visible = hasFilter,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (selectedCategory != null) {
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFF0F9FF),
                        border = BorderStroke(1.dp, secondaryLightSkyBlue)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = if (selectedCategory.type == Type.EXPENSE) generalRed else successGreen,
                                        shape = CircleShape
                                    )
                            )
                            Text(
                                text = "Kategori: ${selectedCategory.name}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = mainSkyBlue
                            )
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hapus filter kategori",
                                tint = mainSkyBlue,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable { onClearCategoryFilter() }
                            )
                        }
                    }
                }
            }
        }
    }
}
