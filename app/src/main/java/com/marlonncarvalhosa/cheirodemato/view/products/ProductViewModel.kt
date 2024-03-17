package com.marlonncarvalhosa.cheirodemato.view.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.usecase.ProductUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productUseCase: ProductUseCase
): ViewModel() {

    private val _productViewState = MutableLiveData<ProductViewState>()
    val productViewState: LiveData<ProductViewState> = _productViewState

    fun getProducts() {
        viewModelScope.launch {
            productUseCase.getProducts()
                .onStart {
                    _productViewState.value = ProductViewState.Loading
                }
                .catch { error ->
                    _productViewState.value = ProductViewState.Error(error.message.toString())
                }
                .collect {
                    _productViewState.value = ProductViewState.SuccessGetProducts(it)
                }
        }
    }

    fun newProduct(id: String, product: ProductModel) {
        viewModelScope.launch {
            productUseCase.newProduct(id, product)
                .onStart {
                    _productViewState.value = ProductViewState.Loading
                }
                .catch { error ->
                    _productViewState.value = ProductViewState.Error(error.message.toString())
                }
                .collect {
                    _productViewState.value = ProductViewState.SuccessNewProduct
                }
        }
    }

    fun updateProduct(id: String, product: HashMap<String, Int?>) {
        viewModelScope.launch {
            productUseCase.updateProduct(id, product)
                .onStart {
                    _productViewState.value = ProductViewState.Loading
                }
                .catch { error ->
                    _productViewState.value = ProductViewState.Error(error.message.toString())
                }
                .collect {
                    _productViewState.value = ProductViewState.SuccessUpdateProduct
                }
        }
    }
}