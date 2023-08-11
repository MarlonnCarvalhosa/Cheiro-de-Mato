package com.marlonncarvalhosa.cheirodemato.view.login

sealed class AuthViewState {
    object Loading : AuthViewState()
    object Success : AuthViewState()
    data class Error(val errorMessage: String) : AuthViewState()
}
