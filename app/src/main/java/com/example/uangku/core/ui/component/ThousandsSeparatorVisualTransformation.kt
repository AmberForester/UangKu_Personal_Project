package com.example.uangku.core.ui.component

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * VisualTransformation untuk menambahkan pemisah ribuan (titik) secara otomatis
 * pada kolom input angka tanpa mengubah nilai string aslinya (tetap deretan digit murni).
 */
class ThousandsSeparatorVisualTransformation : VisualTransformation {

    private val decimalFormat = DecimalFormat("#,###", DecimalFormatSymbols(Locale("id", "ID")))

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        val parsed = originalText.toLongOrNull() ?: return TransformedText(text, OffsetMapping.Identity)
        val formattedString = decimalFormat.format(parsed)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 0) return 0
                val clampedOffset = offset.coerceAtMost(originalText.length)
                val subText = originalText.substring(0, clampedOffset)
                val subParsed = subText.toLongOrNull() ?: return clampedOffset
                val subFormatted = decimalFormat.format(subParsed)
                return subFormatted.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 0) return 0
                val clampedOffset = offset.coerceAtMost(formattedString.length)
                val subText = formattedString.substring(0, clampedOffset)
                val digitsOnly = subText.filter { it.isDigit() }
                return digitsOnly.length.coerceAtMost(originalText.length)
            }
        }

        return TransformedText(AnnotatedString(formattedString), offsetMapping)
    }
}
