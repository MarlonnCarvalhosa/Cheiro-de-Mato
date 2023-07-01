package com.marlonncarvalhosa.cheirodemato.data.repository.datasource

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.marlonncarvalhosa.cheirodemato.data.model.LoginModel
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody

interface LoginRepository {
    suspend fun login(model: LoginModel): Flow<Task<AuthResult>>
}