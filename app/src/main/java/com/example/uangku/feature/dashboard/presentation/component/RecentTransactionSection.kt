package com.example.uangku.feature.dashboard.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.core.ui.theme.secondaryBgSkyBlue
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.transaction.domain.Transaction
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecentTransactionSection(
    transactions: List<Transaction>,
    onSeeAllClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        SectionHeader(
            title = "Transaksi Terbaru",
            onSeeAllClick = onSeeAllClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = secondaryBgSkyBlue,
                    shape = RoundedCornerShape(20.dp)
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            if (transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada transaksi di periode ini",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF64748B)
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    val displayList = transactions.take(5)
                    displayList.forEachIndexed { index, transaction ->
                        TransactionItem(transaction = transaction)
                        if (index < displayList.lastIndex) {
                            HorizontalDivider(
                                color = Color(0xFFF1F5F9),
                                thickness = 1.dp,
                                modifier = Modifier.padding(start = 54.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TransactionItem(
    transaction: Transaction
) {
    val isIncome = transaction.type == Type.INCOME

    val cal = Calendar.getInstance().apply { time = transaction.date }
    val nowCal = Calendar.getInstance()
    val isToday = cal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
            cal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)
    nowCal.add(Calendar.DAY_OF_YEAR, -1)
    val isYesterday = cal.get(Calendar.YEAR) == nowCal.get(Calendar.YEAR) &&
            cal.get(Calendar.DAY_OF_YEAR) == nowCal.get(Calendar.DAY_OF_YEAR)

    val dateStr = when {
        isToday -> "Hari ini"
        isYesterday -> "Kemarin"
        else -> SimpleDateFormat("d MMM yy", Locale("id", "ID")).format(transaction.date)
    }

    val title = if (transaction.description.isNotBlank()) transaction.description else transaction.categoryName
    val subtitle = if (transaction.description.isNotBlank()) "${transaction.categoryName} • $dateStr" else dateStr

    val amountFormatted = currencyMinimalFormatter(transaction.amount)

    Row(
        modifier = Modifier
            .fillMaxWidth()
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

        // Judul & Subtitle
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp
                ),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0F172A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

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

        Spacer(modifier = Modifier.width(8.dp))

        // Nominal (+Rp / -Rp)
        Text(
            text = if (isIncome) "+$amountFormatted" else "-$amountFormatted",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.sp
            ),
            fontWeight = FontWeight.SemiBold,
            color = if (isIncome) successGreen else generalRed
        )
    }
}
