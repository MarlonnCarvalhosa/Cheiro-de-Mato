package com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.OrderRepository
import com.marlonncarvalhosa.cheirodemato.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class OrderRepositoryImpl: OrderRepository {
    private val db = FirebaseFirestore.getInstance()
    override suspend fun getOrders(): Flow<List<OrderModel>> = flow {
        try {
            db.collection(Constants.ORDERS)
                .get()
                .await()
            val querySnapshot = db.collection(Constants.ORDERS).get().await()
            emit(querySnapshot.toObjects(OrderModel::class.java))
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error get order", e)
            throw e
        }
    }

    override suspend fun getOrderById(id: String): Flow<OrderModel> = flow {
        try {
            val documentSnapshot = db.collection(Constants.ORDERS)
                .document(id)
                .get()
                .await()

            val orderModel = documentSnapshot.toObject(OrderModel::class.java)
            if (orderModel != null) {
                emit(orderModel)
            } else {
                Log.e(ContentValues.TAG, "Documento não encontrado ou não pode ser convertido para OrderModel.")
            }

        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Erro ao obter pedido por ID", e)
            throw e
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
            Log.e(ContentValues.TAG, "Error new order", e)
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
            Log.e(ContentValues.TAG, "Error update order", e)
            throw e
        }
    }

    override suspend fun deleteOrder(id: String): Flow<DocumentReference> = flow {
        try {
            db.collection(Constants.ORDERS)
                .document(id)
                .delete()
                .await()
            val documentReference = db.collection(Constants.ORDERS).document(id)
            emit(documentReference)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error delete order", e)
            throw e
        }
    }
}