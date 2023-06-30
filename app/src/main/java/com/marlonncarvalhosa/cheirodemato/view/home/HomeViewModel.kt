package com.marlonncarvalhosa.cheirodemato.view.home

import androidx.lifecycle.ViewModel
import com.marlonncarvalhosa.cheirodemato.data.model.OrderModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel

class HomeViewModel: ViewModel() {

    fun listOrder():MutableList<OrderModel>{
        val list = mutableListOf<OrderModel>()
        list.add(OrderModel(1, "Finalizado", "teste", "R$ 50,00", 4, listProduct(), "30", "06", "23"))
        list.add(OrderModel(2, "Finalizado", "teste", "R$ 50,00", 4, listProduct(), "30", "06", "23"))
        list.add(OrderModel(3, "Finalizado", "teste", "R$ 50,00", 4, listProduct(), "30", "06", "23"))
        list.add(OrderModel(4, "Finalizado", "teste", "R$ 50,00", 4, listProduct(), "30", "06", "23"))
        list.add(OrderModel(5, "Finalizado", "teste", "R$ 50,00", 4, listProduct(), "30", "06", "23"))
        return list
    }

    fun listProduct():MutableList<ProductModel>{
        val list = mutableListOf<ProductModel>()
        list.add(ProductModel("Produto 2", 4, "30", "06", "23", "23"))
        list.add(ProductModel("Produto 3", 4, "30", "06", "23", "23"))
        list.add(ProductModel("Produto 4", 4, "30", "06", "23", "23"))
        return list
    }
}