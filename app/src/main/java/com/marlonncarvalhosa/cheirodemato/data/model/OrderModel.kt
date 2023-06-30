package com.marlonncarvalhosa.cheirodemato.data.model

data class OrderModel(
    val id: Int,
    val status: String,
    val note: String,
    val totalValue: String,
    val amountProduct: Int,
    val items: List<ProductModel>,
    val dia: String,
    val mes: String,
    val ano: String
)
