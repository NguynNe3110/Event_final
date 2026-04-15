package com.uzuu.customer.feature.middle.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.uzuu.customer.domain.repository.CartRepository
import com.uzuu.customer.domain.repository.OrderRepository

class CartFactory(
    private val cartRepo: CartRepository,
    private val orderRepo: OrderRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CartViewModel(cartRepo, orderRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}