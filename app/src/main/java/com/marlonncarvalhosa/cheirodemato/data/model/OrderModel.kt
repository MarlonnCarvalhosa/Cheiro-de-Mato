package com.marlonncarvalhosa.cheirodemato.data.model

data class OrderModel(
    val id: Int? = 0,
    val status: String? = "",
    val note: String? = "",
    var totalValue: Double? = 0.0,
    val amountProduct: Int? = 0,
    val items: List<ProductModel>? = emptyList(),
    val dia: String? = "",
    val mes: String? = "",
    val ano: String? = ""
)
