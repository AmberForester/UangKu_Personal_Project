package com.example.uangku.feature.category.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uangku.core.domain.Type
import com.example.uangku.core.ui.theme.brandSkyBlue
import com.example.uangku.core.ui.theme.generalRed
import com.example.uangku.core.ui.theme.successGreen
import com.example.uangku.feature.category.domain.Category

@Composable
fun CategoryItem(
    category: Category,
    onEditClick: (Category) -> Unit,
    onDeleteClick: (Category) -> Unit
) {
    val isIncome = category.type == Type.INCOME

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar Lingkaran
        Surface(
            shape = CircleShape,
            color = if (isIncome) Color(0xFFDCFCE7) else Color(0xFFFFF3E0),
            modifier = Modifier.size(38.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Label,
                    contentDescription = null,
                    tint = if (isIncome) successGreen else Color(0xFFEA580C),
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(Modifier.width(14.dp))

        // Nama Kategori
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp),
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0F172A),
            modifier = Modifier.weight(1f)
        )

        // Tombol Edit
        Surface(
            shape = CircleShape,
            color = Color(0xFFF0F9FF),
            modifier = Modifier.size(34.dp)
        ) {
            IconButton(
                onClick = { onEditClick(category) },
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Kategori",
                    tint = brandSkyBlue,
                    modifier = Modifier.size(17.dp)
                )
            }
        }

        Spacer(Modifier.width(8.dp))

        // Tombol Hapus
        Surface(
            shape = CircleShape,
            color = Color(0xFFFEE2E2),
            modifier = Modifier.size(34.dp)
        ) {
            IconButton(
                onClick = { onDeleteClick(category) },
                modifier = Modifier.size(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus Kategori",
                    tint = generalRed,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}
