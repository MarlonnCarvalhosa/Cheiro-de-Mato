package com.marlonncarvalhosa.cheirodemato.view.order

import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.view.login.AuthViewState

sealed class OrderViewState {
    object Loading : OrderViewState()
    data class SuccessGetAllOrder(val orders: List<OrderModel>) : OrderViewState()
    data class ErrorGetAllOrder(val errorMessage: String) : OrderViewState()
}