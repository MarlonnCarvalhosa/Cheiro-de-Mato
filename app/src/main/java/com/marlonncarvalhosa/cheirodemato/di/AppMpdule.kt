package com.marlonncarvalhosa.cheirodemato.di

import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.AuthRepository
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.OrderRepository
import com.marlonncarvalhosa.cheirodemato.data.repository.datasource.ProductRepository
import com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl.AuthRepositoryImpl
import com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl.OrderRepositoryImpl
import com.marlonncarvalhosa.cheirodemato.data.repository.datasourceimpl.ProductRepositoryImpl
import com.marlonncarvalhosa.cheirodemato.usecase.OrderUseCase
import com.marlonncarvalhosa.cheirodemato.usecase.ProductUseCase
import com.marlonncarvalhosa.cheirodemato.usecase.SignInUseCase
import com.marlonncarvalhosa.cheirodemato.view.home.HomeViewModel
import com.marlonncarvalhosa.cheirodemato.view.login.AuthViewModel
import com.marlonncarvalhosa.cheirodemato.view.order.OrderViewModel
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
        ProductViewModel(productUseCase = get())
    }
    viewModel {
        OrderViewModel(orderUseCase = get())
    }
}

val repositoryModule = module{
    single<AuthRepository> {
        AuthRepositoryImpl()
    }
    single<OrderRepository> {
        OrderRepositoryImpl()
    }
    single<ProductRepository> {
        ProductRepositoryImpl()
    }
}

val useCase = module {
    single {
        SignInUseCase(authRepository = get())
    }
    single {
        OrderUseCase(orderRepository = get())
    }
    single {
        ProductUseCase(productRepository = get())
    }
}

val listModules = listOf(useCase, repositoryModule, viewModelModule)