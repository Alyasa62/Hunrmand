package com.example.hunrmand.di

import androidx.room.Room
import com.example.hunrmand.data.repository.AuthRepositoryImpl
import com.example.hunrmand.data.repository.JobRepositoryImpl
import com.example.hunrmand.data.repository.LocationRepositoryImpl
import com.example.hunrmand.data.repository.WorkerRepositoryImpl
import com.example.hunrmand.data.source.local.HunrmandDatabase
import com.example.hunrmand.data.source.local.LocalAuthDataSource
import com.example.hunrmand.data.source.local.LocalAuthDataSourceImpl
import com.example.hunrmand.data.source.local.SessionManager
import com.example.hunrmand.domain.repository.AuthRepository
import com.example.hunrmand.domain.repository.JobRepository
import com.example.hunrmand.domain.repository.LocationRepository
import com.example.hunrmand.domain.repository.WorkerRepository
import com.example.hunrmand.ui.maps.MapViewModel
import com.example.hunrmand.ui.screens.home.HomeViewModel
import com.example.hunrmand.ui.screens.search.SearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // 0. Database & Local Data Sources
    single { 
        Room.databaseBuilder(
            androidContext(),
            HunrmandDatabase::class.java,
            "hunrmand.db"
        ).fallbackToDestructiveMigration().build() 
    }
    single { get<HunrmandDatabase>().userDao() }
    single { get<HunrmandDatabase>().jobDao() }
    
    single { SessionManager(androidContext()) }
    single<LocalAuthDataSource> { LocalAuthDataSourceImpl(get()) }

    // 1. Repositories (Singletons - Created once)
    single<WorkerRepository> { WorkerRepositoryImpl(get()) }
    single<LocationRepository> { LocationRepositoryImpl(androidContext()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<JobRepository> { JobRepositoryImpl(get()) }

    // 2. ViewModels (Created when the screen opens)
    viewModel { MapViewModel(
        locationRepository = get()
    ) }  // Injects LocationRepository automatically
    viewModel { HomeViewModel(get()) } // Injects WorkerRepository automatically
    viewModel { SearchViewModel(get()) } // Injects WorkerRepository automatically
    
    // Auth & Jobs
    viewModel { com.example.hunrmand.ui.screens.auth.AuthViewModel(get()) }
    viewModel { com.example.hunrmand.ui.screens.job.PostJobViewModel(get()) }
}