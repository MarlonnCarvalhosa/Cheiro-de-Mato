package com.marlonncarvalhosa.cheirodemato.data.repository.datasource

import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import kotlinx.coroutines.flow.Flow

interface OrdersRepository {
    suspend fun getAllOrders(): Flow<List<OrderModel>>
}