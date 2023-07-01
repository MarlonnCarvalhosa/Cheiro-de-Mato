package com.marlonncarvalhosa.cheirodemato.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.LoginRepository
import com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl.LoginRepositoryImpl
import com.marlonncarvalhosa.cheirodemato.usecase.LoginUseCase
import com.marlonncarvalhosa.cheirodemato.view.home.HomeViewModel
import com.marlonncarvalhosa.cheirodemato.view.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel {
        LoginViewModel(loginUseCase = get())
    }
    viewModel {
        HomeViewModel()
    }
}

val repositoryModule = module{
    single<LoginRepository> {
        LoginRepositoryImpl(auth = Firebase.auth)
    }
}

val useCase = module {
    single {
        LoginUseCase(loginRepository = get())
    }
}

val listModules = listOf(useCase, repositoryModule, viewModelModule)