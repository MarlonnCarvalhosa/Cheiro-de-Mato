package com.marlonncarvalhosa.cheirodemato.util

import java.util.*

object Constants {

    const val FIRST_ACCESS = "firstAccess"
    const val YES = "yes"
    const val NO  = "no"
    const val ITEMS  = "items"
    const val TOTAL_VALUE  = "totalValue"
    const val STATUS  = "status"

    //Mask
    const val FORMAT_CPF = "###.###.###-##"
    const val FORMAT_PHONE = "(##) ####-####"
    const val FORMAT_CELPHONE = "(##) #####-####"
    const val FORMAT_CEP = "#####-###"
    const val FORMAT_UF = "##"
    const val FORMAT_DATE_FULL = "##/##/####"
    const val FORMAT_DATE_CARD = "##/##"
    const val FORMAT_CVV = "###"
    const val FORMAT_HOUR = "##:##:##"
    const val FORMAT_DATE_HOUR = "##/##/## - ##:##:##"
    const val FORMAT_CNPJ = "##.###.###/####-##"
    const val FORMAT_WEIGHT = "###,##"
    const val FORMAT_DECIMAL = "#,###.##"
    const val FORMAT_CARD = "####.####.####.####"
    const val FORMAT_VALIDATION_CARD = "##/####"
    val LOCALE_PT_BR = Locale("pt", "BR")

    //Permissisons
    const val REQUEST_CODE_CAMERA_AND_GALLERY = 200
    const val REQUEST_CODE_GALLERY = 100
    const val REQUEST_CODE_CAMERA = 300
    const val REQUEST_CODE_LOCATION = 400

    //HTTP
    const val HTTP_SUCCESS = 200
    //const val BASE_URL = "https://b9ff-187-111-138-155.sa.ngrok.io"
    const val BASE_URL = "http://3.236.72.190:8080"

    //SHARED PREFERENCES
    const val TOKEN_KEY = "TOKEN"
    const val USER_ID_KEY = "USER_ID"
    const val CUSTOMER_ID = "CUSTOMER_ID"
    const val REGISTER = "REGISTER_TYPE"
    const val USER_TYPE = "USER_TYPE"
    const val PLAN = "PLAN_TYPE"
    const val NAVIGATION = "TYPE"
    const val PRICE = "PRICE"
    const val CREDIT = "CREDIT"

    // Status
    const val STATUS_WAITING = "Aguardando"
    const val STATUS_FINISH = "Finalizado"

    // Collections
    const val ORDERS = "orders"
    const val PRODUCTS = "products"

}