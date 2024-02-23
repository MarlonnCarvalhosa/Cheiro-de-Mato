package com.marlonncarvalhosa.cheirodemato.usecase

import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.OrderRepository
import kotlinx.coroutines.flow.Flow

class OrderUseCase(private val orderRepository: OrderRepository) {
    suspend fun getAllOrders(): Flow<List<OrderModel>> {
        return orderRepository.getAllOrders()
    }
}