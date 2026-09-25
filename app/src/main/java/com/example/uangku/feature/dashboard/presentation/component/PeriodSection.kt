package com.example.uangku.feature.dashboard.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.uangku.feature.period.domain.FinancialPeriod
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PeriodSection(

    financialPeriod: FinancialPeriod?

) {
    val periodFormat = DateTimeFormatter.ofPattern(
        "d MMMM yyyy",
        Locale("id", "ID")
    )

    Column (
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ){

        Text(
            text = "Financial Period",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold
        )

        Text(
            text = financialPeriod?.let {
                "${it.startDate.format(periodFormat)} - " +
                        it.endDate.format(periodFormat)
            } ?: "-",
            style = MaterialTheme.typography.titleSmall
        )
    }
}