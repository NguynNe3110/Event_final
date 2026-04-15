package com.uzuu.customer.data.mapper

import com.uzuu.customer.data.local.entity.TicketEntity
import com.uzuu.customer.data.remote.dto.response.CategoryTicketResponseDto
import com.uzuu.customer.data.remote.dto.response.MyTicketResponseDto
import com.uzuu.customer.domain.model.CategoryTicket
import com.uzuu.customer.domain.model.MyTicket

// ── CategoryTicket ────────────────────────────────────────────────────────────

fun CategoryTicketResponseDto.ticketDtoToDomain(): CategoryTicket {
    return CategoryTicket(
        id                = id,
        name              = name,
        price             = price,
        totalQuantity     = totalQuantity,
        remainingQuantity = remainingQuantity
    )
}

// ── MyTicket DTO → Domain ─────────────────────────────────────────────────────

fun MyTicketResponseDto.toDomain(): MyTicket = MyTicket(
    id             = id,
    eventName      = eventName,
    ticketTypeName = ticketTypeName,
    ticketCode     = ticketCode,
    qrCode         = qrCode,
    status         = status,
    usedAt         = usedAt
)

// ── MyTicket DTO → Entity (để cache) ─────────────────────────────────────────
// eventId và ticketTypeId không có trong MyTicketResponseDto → dùng 0L

fun MyTicketResponseDto.toEntity(): TicketEntity = TicketEntity(
    id             = id,
    eventId        = 0L,
    eventname      = eventName,
    ticketTypeId   = 0L,
    ticketTypeName = ticketTypeName,
    ticketCode     = ticketCode,
    qrCode         = qrCode,
    status         = status,
    usedAt         = usedAt
)

// ── Entity → Domain ───────────────────────────────────────────────────────────

fun TicketEntity.toMyTicketDomain(): MyTicket = MyTicket(
    id             = id,
    eventName      = eventname,
    ticketTypeName = ticketTypeName,
    ticketCode     = ticketCode,
    qrCode         = qrCode,
    status         = status,
    usedAt         = usedAt
)