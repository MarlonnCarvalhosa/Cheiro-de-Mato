package com.marlonncarvalhosa.cheirodemato.view.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel
import com.marlonncarvalhosa.cheirodemato.usecase.OrderUseCase
import com.marlonncarvalhosa.cheirodemato.view.login.AuthViewState
import com.marlonncarvalhosa.cheirodemato.view.order.OrderViewState
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
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
                    _orderViewState.value = OrderViewState.ErrorGetAllOrder(error.message.toString())
                }
                .collect {
                    _orderViewState.value = OrderViewState.SuccessGetAllOrder(it)
                }
        }
    }

}