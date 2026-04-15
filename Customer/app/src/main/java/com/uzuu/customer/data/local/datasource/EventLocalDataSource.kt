package com.uzuu.customer.data.local.datasource

import com.uzuu.customer.data.local.dao.EventDao
import com.uzuu.customer.data.local.entity.EventEntity
import com.uzuu.customer.domain.model.Event
import kotlinx.coroutines.flow.Flow

class EventLocalDataSource(private val eventDao: EventDao) {
    
    fun observeAllEvents(): Flow<List<EventEntity>> = eventDao.observeAllEvents()
    
    suspend fun getAllEvents(): List<EventEntity> = eventDao.getAllEvents()
    
    suspend fun getEventById(id: Long): EventEntity? = eventDao.getEventById(id)
    
    suspend fun cacheEvents(events: List<EventEntity>) {
        eventDao.insertEvents(events)
    }
    
    suspend fun cacheEvent(event: EventEntity) {
        eventDao.insertEvent(event)
    }
    
    suspend fun clearAllEvents() {
        eventDao.deleteAllEvents()
    }
    
    suspend fun hasCachedData(): Boolean = eventDao.getEventCount() > 0
}
