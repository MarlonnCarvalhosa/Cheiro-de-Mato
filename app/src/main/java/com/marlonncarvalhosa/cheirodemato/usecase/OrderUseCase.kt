package com.marlonncarvalhosa.cheirodemato.usecase

import com.google.firebase.firestore.DocumentReference
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.OrderRepository
import kotlinx.coroutines.flow.Flow

class OrderUseCase(private val orderRepository: OrderRepository) {
    suspend fun getAllOrders(): Flow<List<OrderModel>> {
        return orderRepository.getOrders()
    }

    suspend fun getOrderById(id: String): Flow<OrderModel> {
        return orderRepository.getOrderById(id)
    }

    suspend fun newOrder(id: String, order: OrderModel): Flow<DocumentReference> {
        return orderRepository.newOrder(id, order)
    }

    suspend fun updateOrder(id: String, order: Map<String, Any>): Flow<DocumentReference> {
        return orderRepository.updateOrder(id, order)
    }

    suspend fun deleteOrder(id: String): Flow<DocumentReference> {
        return orderRepository.deleteOrder(id)
    }
}