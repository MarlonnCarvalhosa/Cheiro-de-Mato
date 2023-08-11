package com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl : AuthRepository {
    override suspend fun signInWithEmailAndPassword(email: String, password: String): Flow<AuthResult> {
        val auth = FirebaseAuth.getInstance()
        return flow {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            emit(result)
        }
    }
}