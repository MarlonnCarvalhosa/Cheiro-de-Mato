package com.marlonncarvalhosa.cheirodemato.data.repository.datasource

import com.google.firebase.firestore.DocumentReference
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProducts(): Flow<List<ProductModel>>

    suspend fun newProduct(id: String, product: ProductModel): Flow<DocumentReference>

    suspend fun updateProduct(id: String, product: HashMap<String, Int?>): Flow<ProductModel>
}