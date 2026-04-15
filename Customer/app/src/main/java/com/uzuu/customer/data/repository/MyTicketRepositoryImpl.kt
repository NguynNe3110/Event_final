package com.uzuu.customer.data.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.core.result.safeApiCall
import com.uzuu.customer.data.local.datasource.TicketLocalDataSource
import com.uzuu.customer.data.mapper.toDomain
import com.uzuu.customer.data.mapper.toEntity
import com.uzuu.customer.data.mapper.toMyTicketDomain
import com.uzuu.customer.data.remote.datasource.MyTicketRemoteDataSource
import com.uzuu.customer.domain.model.MyTicket
import com.uzuu.customer.domain.repository.MyTicketRepository

class MyTicketRepositoryImpl(
    private val remote: MyTicketRemoteDataSource,
    private val local: TicketLocalDataSource
) : MyTicketRepository {

    override suspend fun getMyTickets(): ApiResult<List<MyTicket>> =
        safeApiCall {
            try {
                val response = remote.getMyTickets()
                if (response.code == 200 || response.code == 0 || response.code == 1000) {
                    val tickets = response.result.map { it.toDomain() }
                    // Cache: xóa cũ rồi lưu mới
                    local.clearAllTickets()
                    local.cacheTickets(response.result.map { it.toEntity() })
                    tickets
                } else {
                    throw Exception(response.message ?: "Không lấy được danh sách vé")
                }
            } catch (e: Exception) {
                // Khi mất mạng → fallback về cache
                val cached = local.getAllTickets()
                if (cached.isNotEmpty()) {
                    cached.map { it.toMyTicketDomain() }
                } else {
                    throw e // Không có cache → trả về lỗi
                }
            }
        }
}