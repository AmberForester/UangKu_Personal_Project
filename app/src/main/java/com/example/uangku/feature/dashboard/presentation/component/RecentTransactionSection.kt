import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.component.currencyFormatter
import com.example.uangku.feature.dashboard.presentation.component.SectionHeader
import com.example.uangku.feature.transaction.domain.Transaction
import java.text.SimpleDateFormat
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecentTransactionSection(
    transactions: List<Transaction>,
    onSeeAllClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
    ) {
        SectionHeader(
            title = "Recent Transactions",
            onSeeAllClick = onSeeAllClick
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        if (transactions.isEmpty()) {
            Text(
                text = "Belum ada transaksi",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            transactions.forEach { transaction ->
                TransactionItem(
                    transaction = transaction
                )
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

    val simpleDateFormat = SimpleDateFormat(
        "d MMM yy",
        Locale("id", "ID")
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isIncome) {
                        Icons.Default.ArrowUpward
                    } else {
                        Icons.Default.ArrowDownward
                    },
                    contentDescription = null,
                    tint = if(isIncome) {
                        Color(0xFF43A047)
                    } else {
                        Color.Red
                    },
                )
            }
        }

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            if(transaction.description.isNotEmpty()){
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.height(2.dp)
                )

                Text(
                    text = "${transaction.categoryName} • ${simpleDateFormat.format(transaction.date)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            else {
                Text(
                    text = "${transaction.categoryName} • ${simpleDateFormat.format(transaction.date)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = if (isIncome) {
                "+${currencyFormatter(transaction.amount)}"
            } else {
                "-${currencyFormatter(transaction.amount)}"
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}