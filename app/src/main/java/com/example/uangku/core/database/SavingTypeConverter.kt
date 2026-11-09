package com.example.uangku.core.database

import androidx.room.TypeConverter
import com.example.uangku.feature.savings.domain.SavingType

class SavingTypeConverter {

    @TypeConverter
    fun fromSavingType(type: SavingType): String = type.name

    @TypeConverter
    fun toSavingType(value: String): SavingType = SavingType.valueOf(value)
}