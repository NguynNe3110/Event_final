package com.uzuu.customer.data.repository

import com.uzuu.customer.data.local.datasource.CategoryLocalDataSource
import com.uzuu.customer.data.mapper.toDomain
import com.uzuu.customer.data.mapper.toEntity
import com.uzuu.customer.data.remote.datasource.CategoryRemoteDataSource
import com.uzuu.customer.domain.model.CategoryItem
import com.uzuu.customer.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val remote: CategoryRemoteDataSource,
    private val local: CategoryLocalDataSource
) : CategoryRepository {
    override suspend fun getAllCategories(): List<CategoryItem> {
        // Ưu tiên: Nếu có dữ liệu cache, trả về ngay để load nhanh
        val cachedCategories = getCachedCategories()
        if (cachedCategories.isNotEmpty()) {
            println("DEBUG [CategoryRepositoryImpl] Returning ${cachedCategories.size} cached categories")
            return cachedCategories
        }

        return try {
            val response = remote.getAllCategories()
            // Thêm "Tất cả" ở đầu danh sách với id = -1
            val allCategory = CategoryItem(id = -1, name = "Tất cả", isSelected = true)
            val fromServer = response.result.map {
                CategoryItem(
                    id = it.id.toInt(),
                    name = it.name,
                    isSelected = false
                )
            }
            val result = listOf(allCategory) + fromServer
            
            // Cache the categories locally (excluding the "Tất cả" item which is added locally)
            val entities = fromServer.map { it.toEntity() }
            local.cacheCategories(entities)
            println("DEBUG [CategoryRepositoryImpl] Cached ${fromServer.size} categories")
            
            result
        } catch (e: Exception) {
            println("DEBUG [CategoryRepositoryImpl] Error fetching categories: ${e.message}")
            // Return cached data on error
            val cachedCategories = getCachedCategories()
            if (cachedCategories.isNotEmpty()) {
                // Add "Tất cả" to cached data
                val allCategory = CategoryItem(id = -1, name = "Tất cả", isSelected = true)
                listOf(allCategory) + cachedCategories
            } else {
                emptyList()
            }
        }
    }

    override suspend fun getCachedCategories(): List<CategoryItem> {
        return try {
            local.getAllCategories().map { it.toDomain(isSelected = false) }
        } catch (e: Exception) {
            println("DEBUG [CategoryRepositoryImpl] Error getting cached categories: ${e.message}")
            emptyList()
        }
    }
}