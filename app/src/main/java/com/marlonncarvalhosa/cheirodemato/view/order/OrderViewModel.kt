package com.marlonncarvalhosa.cheirodemato.view.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.usecase.OrderUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class OrderViewModel(
    private val orderUseCase: OrderUseCase
): ViewModel() {

    private val _orderViewState = MutableLiveData<OrderViewState>()
    val orderViewState: LiveData<OrderViewState> = _orderViewState

    fun getAllOrders() {
        viewModelScope.launch {
            orderUseCase.getAllOrders()
                .onStart {
                    _orderViewState.value = OrderViewState.Loading
                }
                .catch { error ->
                    _orderViewState.value = OrderViewState.Error(error.message.toString())
                }
                .collect {
                    _orderViewState.value = OrderViewState.SuccessGetAllOrders(it)
                }
        }
    }

    fun getOrderById(id: String) {
        viewModelScope.launch {
            orderUseCase.getOrderById(id)
                .onStart {
                    _orderViewState.value = OrderViewState.Loading
                }
                .catch { error ->
                    _orderViewState.value = OrderViewState.Error(error.message.toString())
                }
                .collect {
                    _orderViewState.value = OrderViewState.SuccessGetOrderById(it)
                }
        }
    }

    fun newOrder(id: String, order: OrderModel) {
        viewModelScope.launch {
            orderUseCase.newOrder(id, order)
                .onStart {
                    _orderViewState.value = OrderViewState.Loading
                }
                .catch { error ->
                    _orderViewState.value = OrderViewState.Error(error.message.toString())
                }
                .collect {
                    _orderViewState.value = OrderViewState.SuccessNewOrder
                }
        }
    }

    fun updateOrder(id: String, order: Map<String, Any>) {
        viewModelScope.launch {
            orderUseCase.updateOrder(id, order)
                .onStart {
                    _orderViewState.value = OrderViewState.Loading
                }
                .catch { error ->
                    _orderViewState.value = OrderViewState.Error(error.message.toString())
                }
                .collect {
                    _orderViewState.value = OrderViewState.SuccessUpdateOrder
                }
        }
    }

    fun deleteOrder(id: String) {
        viewModelScope.launch {
            orderUseCase.deleteOrder(id)
                .onStart {
                    _orderViewState.value = OrderViewState.Loading
                }
                .catch { error ->
                    _orderViewState.value = OrderViewState.Error(error.message.toString())
                }
                .collect {
                    _orderViewState.value = OrderViewState.SuccessDeleteOrder
                }
        }
    }
}