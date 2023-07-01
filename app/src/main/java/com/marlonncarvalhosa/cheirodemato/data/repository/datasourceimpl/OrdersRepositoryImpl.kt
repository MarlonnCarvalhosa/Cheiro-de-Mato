package com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.OrdersRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow

class OrdersRepositoryImpl(
    private val db: FirebaseFirestore = Firebase.firestore
): OrdersRepository {
    override suspend fun getAllOrders() = flow<List<OrderModel>> {
        coroutineScope {
            db.collection("orders")
                .get()
                .addOnSuccessListener { result ->
                    val listOrders = mutableListOf<OrderModel>()
                    listOrders.addAll(result.toObjects(OrderModel::class.java))
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "Error getting documents: ", exception)
                }
        }
    }
}