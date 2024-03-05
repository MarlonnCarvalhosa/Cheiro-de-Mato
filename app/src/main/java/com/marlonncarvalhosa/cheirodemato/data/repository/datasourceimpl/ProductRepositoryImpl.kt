package com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.ProductRepository
import com.marlonncarvalhosa.cheirodemato.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ProductRepositoryImpl: ProductRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getProducts(): Flow<List<ProductModel>> = flow {
        try {
            db.collection(Constants.PRODUCTS)
                .get()
                .await()
            val querySnapshot = db.collection(Constants.PRODUCTS).get().await()
            emit(querySnapshot.toObjects(ProductModel::class.java))
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error get product", e)
            throw e
        }
    }

    override suspend fun newProduct(id: String, product: ProductModel): Flow<DocumentReference> = flow {
        try {
            db.collection(Constants.PRODUCTS)
                .document(id)
                .set(product)
                .await()
            val documentReference = db.collection(Constants.PRODUCTS).document(id)
            emit(documentReference)
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Error new product", e)
            throw e
        }
    }
}