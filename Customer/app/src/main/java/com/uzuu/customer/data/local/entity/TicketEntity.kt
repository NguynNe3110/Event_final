package com.uzuu.customer.data.local.entity

import androidx.room.Entity

@Entity
data class TicketEntity (
    val id: Int,
    val idEvent: Int,
    val idCategoryTicket: Int,
    val state: String,
    val price: Int,
)