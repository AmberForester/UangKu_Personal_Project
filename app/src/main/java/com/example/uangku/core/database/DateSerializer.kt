package com.example.uangku.core.database

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateSerializer : KSerializer<Date> {

    private val formatter = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.US
    ).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            "Date",
            PrimitiveKind.STRING
        )

    override fun serialize(
        encoder: Encoder,
        value: Date
    ) {
        encoder.encodeString(
            formatter.format(value)
        )
    }

    override fun deserialize(
        decoder: Decoder
    ): Date {
        return formatter.parse(
            decoder.decodeString()
        ) ?: throw IllegalArgumentException(
            "Invalid date format"
        )
    }
}