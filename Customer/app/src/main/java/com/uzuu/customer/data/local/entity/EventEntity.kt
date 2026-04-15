package com.uzuu.customer.data.local.entity

import androidx.room.Entity

@Entity
data class EventEntity(
    val id: Int,
    val title: String,
    val categoryEvent: String,
    val description: String,
    val dateStartSellTicket: Long,
    val dateEndSellTicket: Long,
    val dateStart: Long,
    val dateEnd: Long,
    val status: String,
    val location: String,
    val img: String
)
