package com.marlonncarvalhosa.cheirodemato.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.marlonncarvalhosa.cheirodemato.data.model.LoginModel
import com.marlonncarvalhosa.cheirodemato.usecase.LoginUseCase
import com.marlonncarvalhosa.cheirodemato.util.Failed
import com.marlonncarvalhosa.cheirodemato.util.Loading
import com.marlonncarvalhosa.cheirodemato.util.Resource
import com.marlonncarvalhosa.cheirodemato.util.Success
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class LoginViewModel(
    private val loginUseCase: LoginUseCase
): ViewModel() {

    private val resultLiveData = MutableStateFlow<Resource<Task<AuthResult>>>(Loading())
    val _resultLiveData: StateFlow<Resource<Task<AuthResult>>>
    get() = resultLiveData

    fun login(model: LoginModel){
        viewModelScope.launch {
            loginUseCase.login(model)
                .onStart {
                    resultLiveData.value = Loading()
                }
                .catch { error ->
                    resultLiveData.value = Failed(error.message.toString())
                }
                .collect {
                    resultLiveData.value = Success(it)
                }
        }
    }
}