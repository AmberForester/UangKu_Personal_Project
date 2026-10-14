package com.example.uangku.feature.transaction.presentation.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.theme.budgetBorderColor
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.successGreen

@Composable
fun FormTypeSelector(
    selectedType: Type,
    onTypeSelected: (Type) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = budgetBorderColor,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Tab Pengeluaran
            TypeTabItem(
                title = "Pengeluaran",
                icon = Icons.Default.ArrowUpward,
                isSelected = selectedType == Type.EXPENSE,
                selectedBgColor = Color(0xFFFEE2E2),
                selectedTextColor = generalRed,
                onClick = { onTypeSelected(Type.EXPENSE) },
                modifier = Modifier.weight(1f)
            )

            // Tab Pemasukan
            TypeTabItem(
                title = "Pemasukan",
                icon = Icons.Default.ArrowDownward,
                isSelected = selectedType == Type.INCOME,
                selectedBgColor = Color(0xFFDCFCE7),
                selectedTextColor = successGreen,
                onClick = { onTypeSelected(Type.INCOME) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun TypeTabItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    selectedBgColor: Color,
    selectedTextColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(13.dp),
        color = if (isSelected) selectedBgColor else Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) selectedTextColor else Color(0xFF64748B),
                modifier = Modifier.size(17.dp)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp
                ),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) selectedTextColor else Color(0xFF64748B)
            )
        }
    }
}
