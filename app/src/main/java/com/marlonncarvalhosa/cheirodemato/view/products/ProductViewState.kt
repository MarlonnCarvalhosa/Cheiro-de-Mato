package com.marlonncarvalhosa.cheirodemato.view.products

import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel

sealed class ProductViewState {
    object Loading : ProductViewState()
    data class SuccessGetProducts(val Products: List<ProductModel>) : ProductViewState()
    data class Error(val errorMessage: String) : ProductViewState()

    object SuccessNewProduct : ProductViewState()
    object SuccessUpdateProduct : ProductViewState()
    object SuccessDeleteProduct : ProductViewState()
}