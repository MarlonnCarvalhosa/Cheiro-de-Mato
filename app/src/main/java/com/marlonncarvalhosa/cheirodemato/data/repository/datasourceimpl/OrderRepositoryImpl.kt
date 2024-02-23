package com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class OrderRepositoryImpl: OrderRepository {
    override suspend fun getAllOrders(): Flow<List<OrderModel>> = flow {
        val db = FirebaseFirestore.getInstance()
        val querySnapshot = db.collection("orders").get().await()

        if (!querySnapshot.isEmpty) {
            val orders = querySnapshot.toObjects(OrderModel::class.java)
            emit(orders)
        } else {
            Log.d(ContentValues.TAG, "No documents found.")
        }
    }
}