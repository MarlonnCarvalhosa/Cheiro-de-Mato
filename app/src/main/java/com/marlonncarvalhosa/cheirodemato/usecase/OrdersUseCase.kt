package com.marlonncarvalhosa.cheirodemato.usecase

import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.OrdersRepository
import kotlinx.coroutines.flow.Flow

class OrdersUseCase(private val ordersRepository: OrdersRepository) {
    suspend fun getAllOrders(): Flow<List<OrderModel>> {
        return ordersRepository.getAllOrders()
    }
}