package com.uzuu.customer.data.mapper

import com.uzuu.customer.data.local.entity.EventEntity
import com.uzuu.customer.data.remote.dto.response.EventResponseDto
import com.uzuu.customer.domain.model.Event

fun EventResponseDto.eventDtoToDomain() : Event {
    return Event(
        id = id,
        name = name,
        categoryName = categoryName,
        location = location,
        startTime = startTime,
        endTime = endTime,
        saleStartDate = saleStartDate,
        saleEndDate = saleEndDate,
        description = description,
        status = status,
        imageUrls = imageUrls,
        ticketTypes = ticketTypes.map { it.ticketDtoToDomain() }
    )
}

fun EventEntity.toDomain(): Event {
    return Event(
        id = id,
        name = name,
        categoryName = categoryName,
        location = location,
        startTime = startTime,
        endTime = endTime,
        saleStartDate = saleStartDate,
        saleEndDate = saleEndDate,
        description = description,
        status = status,
        imageUrls = imageUrls,
        ticketTypes = emptyList() // Ticket types not stored in cache
    )
}

fun Event.toEntity(): EventEntity {
    return EventEntity(
        id = id,
        name = name,
        categoryName = categoryName,
        location = location,
        startTime = startTime,
        endTime = endTime,
        saleStartDate = saleStartDate,
        saleEndDate = saleEndDate,
        description = description,
        status = status,
        imageUrls = imageUrls
    )
}