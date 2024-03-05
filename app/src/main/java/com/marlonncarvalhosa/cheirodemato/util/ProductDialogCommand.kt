package com.marlonncarvalhosa.cheirodemato.util

import com.marlonncarvalhosa.cheirodemato.data.model.ProductModel

sealed class ProductDialogCommand {
    data class ValidationFieldsCommand(val productModel: ProductModel) : ProductDialogCommand()
}