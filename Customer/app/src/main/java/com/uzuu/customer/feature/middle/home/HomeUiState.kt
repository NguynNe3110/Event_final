package com.uzuu.customer.feature.middle.home

import com.uzuu.customer.domain.model.CategoryItem
import com.uzuu.customer.domain.model.Event

data class HomeUiState(
    val isLoading: Boolean = false,
    val isLastPage: Boolean = false,
    val allEvents: List<Event> = emptyList(),
    val events: List<Event> = emptyList(),
    val categories: List<CategoryItem> = emptyList(),
    val selectedCategoryId: Int = -1,
    val searchQuery: String = ""
)