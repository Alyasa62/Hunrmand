package com.example.hunrmand.di

import com.example.hunrmand.data.repository.LocationRepositoryImpl
import com.example.hunrmand.data.repository.WorkerRepositoryImpl
import com.example.hunrmand.domain.repository.LocationRepository
import com.example.hunrmand.domain.repository.WorkerRepository
import com.example.hunrmand.ui.maps.MapViewModel
import com.example.hunrmand.ui.screens.home.HomeViewModel
import com.example.hunrmand.ui.screens.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // 1. Repositories (Singletons - Created once)
    single<WorkerRepository> { WorkerRepositoryImpl() }
    single<LocationRepository> { LocationRepositoryImpl(androidContext()) }

    // 2. ViewModels (Created when the screen opens)
    viewModel { MapViewModel(
        locationRepository = get()
    ) }  // Injects LocationRepository automatically
    viewModel { HomeViewModel(get()) } // Injects WorkerRepository automatically
    viewModel { SearchViewModel(get()) } // Injects WorkerRepository automatically
}