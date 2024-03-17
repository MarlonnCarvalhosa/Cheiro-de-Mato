package com.marlonncarvalhosa.cheirodemato.usecase

import com.google.firebase.firestore.DocumentReference
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.ProductRepository
import kotlinx.coroutines.flow.Flow

class ProductUseCase(private val productRepository: ProductRepository) {
    suspend fun getProducts(): Flow<List<ProductModel>> {
        return productRepository.getProducts()
    }

    suspend fun newProduct(id: String, product: ProductModel): Flow<DocumentReference> {
        return productRepository.newProduct(id, product)
    }

    suspend fun updateProduct(id: String, product: HashMap<String, Int?>): Flow<ProductModel> {
        return productRepository.updateProduct(id, product)
    }
}