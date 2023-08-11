package com.marlonncarvalhosa.cheirodemato.data.repository.datasource

import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithEmailAndPassword(email: String, password: String): Flow<AuthResult>
}