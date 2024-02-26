package com.marlonncarvalhosa.cheirodemato.data.repository.datasource

import com.google.firebase.firestore.DocumentReference
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun getAllOrders(): Flow<List<OrderModel>>

    suspend fun newOrder(id: String, order: OrderModel): Flow<DocumentReference>

    suspend fun updateOrder(id: String, order: Map<String, Any>): Flow<DocumentReference>
}