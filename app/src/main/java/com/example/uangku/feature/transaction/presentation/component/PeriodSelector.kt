package com.example.uangku.feature.transaction.presentation.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.uangku.feature.period.domain.FinancialPeriod
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PeriodSelector (

    financialPeriod: FinancialPeriod,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit

) {
    val yearFormat = DateTimeFormatter.ofPattern(
        "yyyy",
        Locale("id", "ID")
    )

    val periodFormat = DateTimeFormatter.ofPattern(
        "d MMM",
        Locale("id", "ID")
    )

    Column (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = financialPeriod.startDate.format(yearFormat),
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            IconButton(
                onClick = onPreviousClick
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous period"
                )
            }

            Text(
                text = "${financialPeriod.startDate.format(periodFormat)} - " +
                        "${financialPeriod.endDate.format(periodFormat)}",
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(
                onClick = onNextClick
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next period"
                )
            }
        }
    }

}