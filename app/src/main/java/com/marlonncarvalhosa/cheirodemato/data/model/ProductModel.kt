package com.marlonncarvalhosa.cheirodemato.data.model

data class ProductModel(
    val id: Int? = 0,
    val name: String? = "",
    val type: String? = "",
    val amount: Int? = 0,
    val price: Double? = 0.0,
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
