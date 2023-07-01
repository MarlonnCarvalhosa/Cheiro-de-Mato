package com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.marlonncarvalhosa.cheirodemato.data.model.LoginModel
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.LoginRepository
import com.marlonncarvalhosa.cheirodemato.util.Failed
import com.marlonncarvalhosa.cheirodemato.util.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class LoginRepositoryImpl(
    private var auth: FirebaseAuth
): LoginRepository {
    override suspend fun login(model: LoginModel) = flow<Task<AuthResult>> {
        auth.signInWithEmailAndPassword(model.email, model.password)
            .addOnCompleteListener { task ->
                CoroutineScope(Dispatchers.Main).launch {
                    emit(task)
                }
            }
    }
}