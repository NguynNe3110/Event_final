package com.uzuu.customer.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uzuu.customer.data.local.entity.EventEntity
import com.uzuu.customer.data.remote.dto.response.EventResponseDto
import com.uzuu.customer.domain.model.Event

private val gson = Gson()

fun EventResponseDto.eventDtoToDomain(): Event {
    return Event(
        id            = id,
        name          = name,
        categoryName  = categoryName,
        location      = location,
        startTime     = startTime,
        endTime       = endTime,
        saleStartDate = saleStartDate,
        saleEndDate   = saleEndDate,
        description   = description,
        status        = status,
        imageUrls     = imageUrls,
        ticketTypes   = ticketTypes.map { it.ticketDtoToDomain() }
    )
}

/** EventEntity.imageUrls lưu dạng JSON String → giải mã lại thành List<String> */
fun EventEntity.toDomain(): Event {
    val imageList: List<String> = try {
        gson.fromJson(imageUrls, object : TypeToken<List<String>>() {}.type) ?: emptyList()
    } catch (e: Exception) {
        emptyList()
    }
    return Event(
        id            = id,
        name          = name,
        categoryName  = categoryName,
        location      = location,
        startTime     = startTime,
        endTime       = endTime,
        saleStartDate = saleStartDate,
        saleEndDate   = saleEndDate,
        description   = description,
        status        = status,
        imageUrls     = imageList,
        ticketTypes   = emptyList() // Ticket types không lưu trong cache
    )
}

/** Event.imageUrls là List<String> → serialize thành JSON String để lưu DB */
fun Event.toEntity(): EventEntity {
    return EventEntity(
        id            = id,
        name          = name,
        categoryName  = categoryName,
        location      = location,
        startTime     = startTime,
        endTime       = endTime,
        saleStartDate = saleStartDate,
        saleEndDate   = saleEndDate,
        description   = description,
        status        = status,
        imageUrls     = gson.toJson(imageUrls)
    )
}