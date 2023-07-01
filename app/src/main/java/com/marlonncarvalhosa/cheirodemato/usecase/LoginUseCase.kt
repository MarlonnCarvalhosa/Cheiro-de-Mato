package com.marlonncarvalhosa.cheirodemato.usecase

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.marlonncarvalhosa.cheirodemato.data.model.LoginModel
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.LoginRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

class LoginUseCase(private val loginRepository: LoginRepository) {
    suspend fun login(model: LoginModel): Flow<Task<AuthResult>>{
        return loginRepository.login(model)
    }
}