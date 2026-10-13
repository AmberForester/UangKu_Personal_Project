package com.example.uangku.feature.dashboard.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.ui.theme.activeGreen
import com.example.uangku.core.ui.theme.periodBadgeBg
import com.example.uangku.core.ui.theme.periodBadgeText
import com.example.uangku.feature.period.domain.FinancialPeriod
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PeriodBadge(
    financialPeriod: FinancialPeriod?,
    modifier: Modifier = Modifier
) {
    val periodFormat = DateTimeFormatter.ofPattern(
        "d MMM yyyy",
        Locale("id", "ID")
    )

    val dateRangeText = financialPeriod?.let {
        "${it.startDate.format(periodFormat)} - ${it.endDate.format(periodFormat)}"
    } ?: "Memuat periode..."

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = periodBadgeBg,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Periode Keuangan",
                tint = periodBadgeText,
                modifier = Modifier.size(15.dp)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = dateRangeText,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 11.5.sp
                ),
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = "Aktif",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp
                ),
                fontWeight = FontWeight.Bold,
                color = activeGreen
            )
        }
    }
}
