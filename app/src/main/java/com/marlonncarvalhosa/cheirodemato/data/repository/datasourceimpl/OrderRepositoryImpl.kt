package com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.OrderRepository
import com.marlonncarvalhosa.cheirodemato.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class OrderRepositoryImpl: OrderRepository {
    private val db = FirebaseFirestore.getInstance()
    override suspend fun getAllOrders(): Flow<List<OrderModel>> = flow {
        val querySnapshot = db.collection(Constants.ORDERS).get().await()

        if (!querySnapshot.isEmpty) {
            val orders = querySnapshot.toObjects(OrderModel::class.java)
            emit(orders)
        } else {
            Log.d(ContentValues.TAG, "No documents found.")
        }
    }

    override suspend fun newOrder(id: String, order: OrderModel): Flow<DocumentReference> = flow {
        try {
            db.collection(Constants.ORDERS)
                .document(id)
                .set(order)
                .await()
            val documentReference = db.collection(Constants.ORDERS).document(id)
            emit(documentReference)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error adding order", e)
            throw e
        }
    }

    override suspend fun updateOrder(id: String, order: Map<String, Any>): Flow<DocumentReference> = flow {
        try {
            db.collection(Constants.ORDERS)
                .document(id)
                .update(order)
                .await()
            val documentReference = db.collection(Constants.ORDERS).document(id)
            emit(documentReference)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error adding order", e)
            throw e
        }
    }
}