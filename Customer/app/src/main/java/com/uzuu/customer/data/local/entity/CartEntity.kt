package com.uzuu.customer.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.uzuu.customer.data.local.Converters

@Entity(tableName = "carts")
data class CartEntity(
    @PrimaryKey
    val id: Long,
    val totalAmount: Double,
    val updatedAt: Long = System.currentTimeMillis()
)
