package com.example.orgs.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "product_name")
    val name: String,
    @ColumnInfo(name = "product_desc")
    val description: String,
    @ColumnInfo(name = "product_price")
    val price: BigDecimal,
    @ColumnInfo(name = "product_image")
    val image: String?,
)