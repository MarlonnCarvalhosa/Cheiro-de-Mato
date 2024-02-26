package com.marlonncarvalhosa.cheirodemato.data.model

data class ProductModel(
    val id: Int? = 0,
    val name: String? = "",
    val type: String? = "",
    var amount: Int? = 0,
    var price: Double? = 0.0,
    val dia: String? = "",
    val mes: String? = "",
    val ano: String? = ""
)
{
    override fun toString(): String {
        super.toString()
        return name.toString()
    }
}
