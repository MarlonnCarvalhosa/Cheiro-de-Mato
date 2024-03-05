package com.marlonncarvalhosa.cheirodemato.data.repository.datasource

import com.google.firebase.firestore.DocumentReference
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun getOrders(): Flow<List<OrderModel>>

    suspend fun getOrderById(id: String): Flow<OrderModel>

    suspend fun newOrder(id: String, order: OrderModel): Flow<DocumentReference>

    suspend fun updateOrder(id: String, order: Map<String, Any>): Flow<DocumentReference>

    suspend fun deleteOrder(id: String): Flow<DocumentReference>
}