package com.marlonncarvalhosa.cheirodemato.data.model

data class ProductModel(
    val name: String,
    val amount: Int,
    val price: String? = null,
    val dia: String,
    val mes: String,
    val ano: String
)
