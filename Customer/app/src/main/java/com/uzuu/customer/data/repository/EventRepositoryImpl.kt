package com.uzuu.customer.data.repository

import com.uzuu.customer.data.local.datasource.EventLocalDataSource
import com.uzuu.customer.data.mapper.toEntity
import com.uzuu.customer.data.mapper.eventDtoToDomain
import com.uzuu.customer.data.remote.datasource.EventRemoteDataSource
import com.uzuu.customer.domain.model.Event
import com.uzuu.customer.domain.model.PagedResult
import com.uzuu.customer.domain.repository.EventRepository

class EventRepositoryImpl(
    private val eventRemote: EventRemoteDataSource,
    private val eventLocal: EventLocalDataSource
) : EventRepository {

    override suspend fun getEvent(page: Int): PagedResult<Event> {
        println("DEBUG [EventRepositoryImpl] getEvent(page=$page) called")

        return try {
            val response = eventRemote.getEvent(page)
            println("DEBUG [EventRepositoryImpl] raw response — code=${response.code}, message='${response.message}'")

            val pageData = response.result
            println("DEBUG [EventRepositoryImpl] result — content=${pageData.content.size}, totalPages=${pageData.totalPages}, totalElements=${pageData.totalElements}, isLast=${pageData.last}, currentPage=${pageData.number}")

            if (pageData.content.isNotEmpty()) {
                // Cache the events locally
                val events = pageData.content.map { it.eventDtoToDomain() }
                val entities = events.map { it.toEntity() }
                eventLocal.cacheEvents(entities)
                println("DEBUG [EventRepositoryImpl] Cached ${events.size} events")
                
                PagedResult(
                    data = events,
                    page = pageData.number,
                    totalPages = pageData.totalPages,
                    totalElements = pageData.totalElements,
                    isLast = pageData.last
                )
            } else {
                println("DEBUG [EventRepositoryImpl] WARNING: content is empty!")
                // If API returns empty, try to load from cache
                val cachedEvents = getCachedEvents()
                PagedResult(
                    data = cachedEvents,
                    page = 0,
                    totalPages = 1,
                    totalElements = cachedEvents.size.toLong(),
                    isLast = true
                )
            }
        } catch (e: Exception) {
            println("DEBUG [EventRepositoryImpl] Error fetching events: ${e.message}")
            // Return cached data on error
            val cachedEvents = getCachedEvents()
            PagedResult(
                data = cachedEvents,
                page = 0,
                totalPages = 1,
                totalElements = cachedEvents.size.toLong(),
                isLast = true
            )
        }
    }

    override suspend fun getCachedEvents(): List<Event> {
        return try {
            eventLocal.getAllEvents().map { it.toDomain() }
        } catch (e: Exception) {
            println("DEBUG [EventRepositoryImpl] Error getting cached events: ${e.message}")
            emptyList()
        }
    }
}