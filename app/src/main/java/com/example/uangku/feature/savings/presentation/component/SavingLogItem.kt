package com.example.uangku.feature.savings.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.ui.component.currencyMinimalFormatter
import com.example.uangku.core.ui.theme.GreenComponent
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.greenBg
import com.example.uangku.core.ui.theme.redBg
import com.example.uangku.feature.savings.domain.SavingLog
import com.example.uangku.feature.savings.domain.SavingType
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun SavingLogItem(
    log: SavingLog,
    modifier: Modifier = Modifier
) {
    val isDeposit = log.type == SavingType.DEPOSIT
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")) }
    val accentColor = if (isDeposit) GreenComponent else generalRed
    val accentBg = if (isDeposit) greenBg else redBg

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Circle
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(accentBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isDeposit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        // Note & Date
        Column(
            modifier = Modifier.weight(1f)
        ) {
            val titleText = if (!log.description.isNullOrBlank()) {
                log.description
            } else if (isDeposit) {
                "Setor Tabungan"
            } else {
                "Tarik Tabungan"
            }
            Text(
                text = titleText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = dateFormat.format(log.date),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.width(8.dp))

        // Nominal
        val sign = if (isDeposit) "+" else "-"
        Text(
            text = "$sign ${currencyMinimalFormatter(log.amount)}",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = accentColor
        )
    }
}
