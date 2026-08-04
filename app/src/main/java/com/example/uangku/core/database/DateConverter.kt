package com.example.uangku.core.database

import androidx.room.TypeConverter
import java.util.Date

class DateConverter(){

    @TypeConverter
    fun fromDate(date: Date) : Long {
        return date.time
    }

    @TypeConverter
    fun toDate(value: Long) : Date {
        return Date(value)
    }
}