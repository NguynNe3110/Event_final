package com.uzuu.customer.data.repository

import com.uzuu.customer.core.result.ApiResult
import com.uzuu.customer.core.result.safeApiCall
import com.uzuu.customer.data.local.datasource.OrderLocalDataSource
import com.uzuu.customer.data.local.entity.OrderEntity
import com.uzuu.customer.data.remote.datasource.OrderRemoteDataSource
import com.uzuu.customer.data.remote.dto.response.OrderResponseDto
import com.uzuu.customer.domain.model.Order
import com.uzuu.customer.domain.model.PagedResult
import com.uzuu.customer.domain.repository.OrderRepository

class OrderRepositoryImpl(
    private val remote: OrderRemoteDataSource,
    private val local: OrderLocalDataSource
) : OrderRepository {

    private fun isOk(code: Int) = code == 200 || code == 0 || code == 1000

    // ── Checkout all ──────────────────────────────────────────────────────────

    override suspend fun checkout(paymentMethod: String): ApiResult<Order> =
        safeApiCall {
            val r = remote.checkout(paymentMethod)
            if (isOk(r.code)) {
                val order = r.result.toDomain()
                local.cacheOrder(order.toEntity())
                order
            } else throw Exception(r.message ?: "Thanh toán thất bại")
        }

    // ── Checkout selected ─────────────────────────────────────────────────────

    override suspend fun checkoutSelected(
        paymentMethod: String,
        itemIds: List<Long>
    ): ApiResult<Order> =
        safeApiCall {
            val r = remote.checkoutSelected(paymentMethod, itemIds)
            if (isOk(r.code)) {
                val order = r.result.toDomain()
                local.cacheOrder(order.toEntity())
                order
            } else throw Exception(r.message ?: "Thanh toán thất bại")
        }

    // ── Get orders (paged) ────────────────────────────────────────────────────

    override suspend fun getMyOrders(page: Int): ApiResult<PagedResult<Order>> =
        safeApiCall {
            try {
                val r = remote.getMyOrders(page)
                if (isOk(r.code)) {
                    val p = r.result
                    val orders = p.content.map { it.toDomain() }
                    // Cache: chỉ xóa + ghi lại khi load trang đầu
                    if (page <= 1) local.clearAllOrders()
                    local.cacheOrders(orders.map { it.toEntity() })
                    PagedResult(
                        data          = orders,
                        page          = p.number,
                        totalPages    = p.totalPages,
                        totalElements = p.totalElements,
                        isLast        = p.last
                    )
                } else throw Exception(r.message ?: "Không lấy được lịch sử đơn hàng")
            } catch (e: Exception) {
                // Fallback về cache chỉ ở trang đầu
                if (page <= 1) {
                    val cached = local.getAllOrders()
                    if (cached.isNotEmpty()) {
                        val orders = cached.map { it.toDomain() }
                        return@safeApiCall PagedResult(
                            data          = orders,
                            page          = 0,
                            totalPages    = 1,
                            totalElements = orders.size.toLong(),
                            isLast        = true
                        )
                    }
                }
                throw e
            }
        }
}

// ── Extension functions (private) ─────────────────────────────────────────────

private fun OrderResponseDto.toDomain() = Order(
    id            = id,
    totalAmount   = totalAmount,
    paymentMethod = paymentMethod,
    paymentStatus = paymentStatus,
    orderStatus   = orderStatus,
    orderDate     = orderDate
)

private fun Order.toEntity() = OrderEntity(
    id            = id,
    totalAmount   = totalAmount,
    paymentMethod = paymentMethod,
    paymentStatus = paymentStatus,
    orderStatus   = orderStatus,
    orderDate     = orderDate
)

private fun OrderEntity.toDomain() = Order(
    id            = id,
    totalAmount   = totalAmount,
    paymentMethod = paymentMethod,
    paymentStatus = paymentStatus,
    orderStatus   = orderStatus,
    orderDate     = orderDate
)