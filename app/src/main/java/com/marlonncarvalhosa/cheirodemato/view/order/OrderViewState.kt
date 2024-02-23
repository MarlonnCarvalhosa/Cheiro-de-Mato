package com.marlonncarvalhosa.cheirodemato.view.order

import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel

sealed class OrderViewState {
    object Loading : OrderViewState()
    data class SuccessGetAllOrders(val orders: List<OrderModel>) : OrderViewState()
    data class Error(val errorMessage: String) : OrderViewState()

    object SuccessNewOrder : OrderViewState()
}