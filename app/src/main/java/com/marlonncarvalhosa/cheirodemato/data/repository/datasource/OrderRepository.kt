package com.marlonncarvalhosa.cheirodemato.data.repository.datasource

import com.google.firebase.firestore.DocumentReference
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface OrderRepository {
    suspend fun getAllOrders(): Flow<List<OrderModel>>
    suspend fun newOrders(id: String, order: OrderModel): Flow<DocumentReference>
}