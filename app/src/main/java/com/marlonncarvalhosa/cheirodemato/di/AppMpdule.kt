package com.marlonncarvalhosa.cheirodemato.di

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.AuthRepository
import com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl.AuthRepositoryImpl
import com.marlonncarvalhosa.cheirodemato.usecase.SignInUseCase
import com.marlonncarvalhosa.cheirodemato.view.home.HomeViewModel
import com.marlonncarvalhosa.cheirodemato.view.login.AuthViewModel
import com.marlonncarvalhosa.cheirodemato.view.products.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel {
        AuthViewModel(signInUseCase = get())
    }
    viewModel {
        HomeViewModel()
    }
    viewModel {
        ProductViewModel()
    }
}

val repositoryModule = module{
    single<AuthRepository> {
        AuthRepositoryImpl()
    }
}

val useCase = module {
    single {
        SignInUseCase(authRepository = get())
    }
}

val listModules = listOf(useCase, repositoryModule, viewModelModule)