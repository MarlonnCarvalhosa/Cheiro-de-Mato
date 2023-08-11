package com.marlonncarvalhosa.cheirodemato.usecase

import com.google.firebase.auth.AuthResult
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.AuthRepository
import kotlinx.coroutines.flow.Flow

class SignInUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Flow<AuthResult> {
        return authRepository.signInWithEmailAndPassword(email, password)
    }
}