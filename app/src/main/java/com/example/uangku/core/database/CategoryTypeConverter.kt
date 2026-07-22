package com.example.uangku.core.database

import androidx.room.TypeConverter
import com.example.uangku.core.domain.Type


class CategoryTypeConverter {

    @TypeConverter
    fun fromType(type: Type): String {
        return type.name
    }

    @TypeConverter
    fun toType(value: String): Type {
        return Type.valueOf(value)
    }

}