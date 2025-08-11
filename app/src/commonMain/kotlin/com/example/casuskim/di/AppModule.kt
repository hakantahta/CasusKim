package com.example.casuskim.di

import com.example.casuskim.domain.repository.CategoryRepository
import com.example.casuskim.domain.repository.TaskRepository
import com.example.casuskim.domain.repository.GameRepository
import com.example.casuskim.domain.usecase.GetCategoriesUseCase
import com.example.casuskim.domain.usecase.GetTasksUseCase
import com.example.casuskim.domain.usecase.GameUseCase
import org.koin.dsl.module

val appModule = module {
    // Use Cases
    single { GetCategoriesUseCase(get()) }
    single { GetTasksUseCase(get()) }
    single { GameUseCase(get(), get()) }
    
    // Repositories (şimdilik boş, implementasyonlar daha sonra eklenecek)
    // single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    // single<TaskRepository> { TaskRepositoryImpl(get()) }
    // single<GameRepository> { GameRepositoryImpl(get()) }
    
    // Database (şimdilik boş, SQLDelight implementasyonu daha sonra eklenecek)
    // single { createDatabase(get()) }
    // single { createCategoryQueries(get()) }
    // single { createTaskQueries(get()) }
    // single { createGameQueries(get()) }
}
