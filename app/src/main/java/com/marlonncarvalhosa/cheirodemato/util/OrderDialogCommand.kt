package com.marlonncarvalhosa.cheirodemato.util

import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel

sealed class OrderDialogCommand {
    data class ValidationFieldsCommand(val productModel: ProductModel) : OrderDialogCommand()
}
