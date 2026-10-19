package com.example.uangku.feature.transaction.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.core.ui.component.toDateFormat
import com.example.uangku.core.ui.theme.mainSkyBlue
import com.example.uangku.core.ui.theme.secondaryBgSkyBlue
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.transaction.domain.Transaction
import com.example.uangku.feature.transaction.presentation.TransactionState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionContent(
    state: TransactionState,
    onResetFilter: () -> Unit = {},
    onClick: (Transaction) -> Unit
) {
    val itemsToDisplay = state.filteredTransactions
    val groupedByDate = itemsToDisplay.groupBy {
        it.date.toDateFormat()
    }

    if (state.transactions.isEmpty()) {
        TransactionEmptyState()
    } else if (itemsToDisplay.isEmpty()) {
        TransactionFilterEmptyState(onResetFilter = onResetFilter)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 4.dp, bottom = 88.dp)
        ) {
            groupedByDate.forEach { (_, transactions) ->
                val firstDate = transactions.first().date

                item {
                    TransactionDateHeader(date = firstDate)
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .border(
                                width = 1.dp,
                                color = secondaryBgSkyBlue,
                                shape = RoundedCornerShape(18.dp)
                            ),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        ) {
                            transactions.forEachIndexed { index, transaction ->
                                TransactionItem(
                                    transaction = transaction,
                                    onClick = onClick
                                )
                                if (index < transactions.lastIndex) {
                                    HorizontalDivider(
                                        color = Color(0xFFF1F5F9),
                                        thickness = 1.dp,
                                        modifier = Modifier.padding(start = 54.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(14.dp))
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: (Transaction) -> Unit
) {
    val isIncome = transaction.type == Type.INCOME
    val title = if (transaction.description.isNotBlank()) transaction.description else transaction.categoryName
    val subtitle = if (transaction.description.isNotBlank()) transaction.categoryName else ""
    val amountFormatted = currencyMinimalFormatter(transaction.amount)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(transaction) }
            .padding(vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Circle Avatar
        Surface(
            modifier = Modifier.size(42.dp),
            shape = CircleShape,
            color = if (isIncome) Color(0xFFE8F5E9) else Color(0xFFF1F5F9)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isIncome) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    contentDescription = null,
                    tint = if (isIncome) successGreen else Color(0xFF64748B),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Judul & Kategori
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 14.5.sp
                ),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0F172A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            if (subtitle != "") {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp
                    ),
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Nominal
        Text(
            text = (if (isIncome) "+" else "-") + amountFormatted,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 14.sp
            ),
            fontWeight = FontWeight.Bold,
            color = if (isIncome) successGreen else generalRed
        )
    }
}

@Composable
fun TransactionDateHeader(
    date: Date
) {
    Text(
        text = formatHeaderDate(date),
        style = MaterialTheme.typography.labelLarge.copy(
            fontSize = 12.5.sp
        ),
        fontWeight = FontWeight.Bold,
        color = Color(0xFF64748B),
        modifier = Modifier
            .padding(start = 24.dp, bottom = 6.dp)
    )
}

private fun formatHeaderDate(date: Date): String {
    val cal = Calendar.getInstance().apply { time = date }
    val nowCal = Calendar.getInstance()

    val isToday = cal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
            cal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)

    nowCal.add(Calendar.DAY_OF_YEAR, -1)
    val isYesterday = cal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
            cal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)

    val fullDate = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")).format(date)
    val dayOfWeek = SimpleDateFormat("EEEE", Locale("id", "ID")).format(date)

    return when {
        isToday -> "Hari ini • $fullDate"
        isYesterday -> "Kemarin • $fullDate"
        else -> "$dayOfWeek, $fullDate"
    }
}

@Composable
fun TransactionEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFE0F2FE),
            modifier = Modifier.size(72.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.FolderOpen,
                    contentDescription = null,
                    tint = mainSkyBlue,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Belum Ada Transaksi",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 16.sp
            ),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Ketuk tombol + di bawah untuk mencatat transaksi baru pada periode ini.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp
            ),
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TransactionFilterEmptyState(
    onResetFilter: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp, vertical = 48.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFE0F2FE),
            modifier = Modifier.size(72.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.SearchOff,
                    contentDescription = null,
                    tint = mainSkyBlue,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Tidak Ada Transaksi yang Cocok",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 16.sp
            ),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Tidak ditemukan transaksi untuk kata kunci atau kategori yang sedang dipilih.",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp
            ),
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(18.dp))

        Button(
            onClick = onResetFilter,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = mainSkyBlue,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Hapus Filter",
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp
            )
        }
    }
}
