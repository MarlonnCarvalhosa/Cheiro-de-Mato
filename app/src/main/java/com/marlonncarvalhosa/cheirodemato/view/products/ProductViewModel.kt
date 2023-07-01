package com.marlonncarvalhosa.cheirodemato.view.products

import androidx.lifecycle.ViewModel
import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel

class ProductViewModel: ViewModel() {

    fun listProduct():MutableList<ProductModel>{
        val list = mutableListOf<ProductModel>()
        list.add(ProductModel(1,"Produto 2","Peso", 10000, 50.00, "06", "23", "23"))
        list.add(ProductModel(2,"Produto 3","Peso", 3000, 50.00, "06", "23", "23"))
        list.add(ProductModel(3,"Produto 4","Unidade", 4, 50.00, "06", "23", "23"))
        list.add(ProductModel(3,"Produto 4","Unidade", 4, 50.00, "06", "23", "23"))
        return list
    }
}