package com.marlonncarvalhosa.cheirodemato.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marlonncarvalhosa.cheirodemato.usecase.SignInUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AuthViewModel(private val signInUseCase: SignInUseCase) : ViewModel() {
    private val _authViewState = MutableLiveData<AuthViewState>()
    val authViewState: LiveData<AuthViewState> = _authViewState

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase(email, password)
                .onStart {
                    _authViewState.value = AuthViewState.Loading
                }
                .catch { error ->
                    _authViewState.value = AuthViewState.Error(error.message.toString())
                }
                .collect {
                    _authViewState.value = AuthViewState.Success
                }
        }
    }
}