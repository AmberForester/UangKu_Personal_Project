package com.example.uangku.feature.transaction.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.uangku.core.ui.component.toDateFormat
import com.example.uangku.feature.transaction.domain.Transaction
import com.example.uangku.feature.transaction.presentation.TransactionState
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TransactionContent(
    state: TransactionState,
    onClick: (Transaction) -> Unit
) {

    val groupedByDate = state.transactions.groupBy {
        it.date.toDateFormat()
    }

    if(state.transactions.isEmpty()){
        TransactionEmptyState()
    } else{
        LazyColumn {
            groupedByDate.forEach{ (date, transactions) ->
                item {
                    TransactionDateHeader(date)
                }
                items(transactions){ transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onClick = onClick
                    )
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
    ListItem(
        modifier = Modifier.clickable { onClick(transaction) },
        headlineContent = {
            Text(text = transaction.categoryName)
        },

        supportingContent = {
            Text(transaction.description)
        },

        trailingContent = {
            Text(
                text = NumberFormat
                .getCurrencyInstance(Locale("id", "ID"))
                .format(transaction.amount)
            )
        }
    )
}

@Composable
fun TransactionEmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Outlined.FolderOpen,
            contentDescription = null
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "No transactions yet",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Tap the + button to create your first transaction.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TransactionDateHeader(
    date: String
) {
    Text(
        text = date,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 12.dp
        )
    )
}