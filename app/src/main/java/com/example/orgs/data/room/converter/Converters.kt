package com.example.orgs.data.room.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class Converters {

    @TypeConverter
    fun toBigDecimal(value:Double?):BigDecimal{
        return value?.let { BigDecimal(value.toString())} ?: BigDecimal.ZERO
    }

    @TypeConverter
    fun toDouble(bigDecimal: BigDecimal?):Double?{
        return bigDecimal?.toDouble()
    }
}