package com.marlonncarvalhosa.cheirodemato.data.model

import java.io.Serializable

data class OrderModel(
    val id: Int? = 0,
    val status: String? = "",
    val note: String? = "",
    var totalValue: Double? = 0.0,
    val items: List<ProductModel>? = emptyList(),
    val day: String? = "",
    val month: String? = "",
    val monthName: String? = "",
    val year: String? = ""
): Serializable
