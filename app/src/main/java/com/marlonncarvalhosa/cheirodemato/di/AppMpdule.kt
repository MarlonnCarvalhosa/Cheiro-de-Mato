package com.marlonncarvalhosa.cheirodemato.di

import com.marlonncarvalhosa.cheirodemato.view.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module{
    viewModel {
        HomeViewModel()
    }
}

val repositoryModule = module{
//    single<LoginRepository> {
//        LoginRepositoryImpl(loginService = get())
//    }
}

val useCase = module {
//    single {
//        LoginUseCase(loginRepository = get())
//    }
}

val serviceModule = module{
//    single {
//        ApiFactory.create(LoginService::class.java)
//    }
}

val listModules = listOf(serviceModule, useCase, repositoryModule, viewModelModule)