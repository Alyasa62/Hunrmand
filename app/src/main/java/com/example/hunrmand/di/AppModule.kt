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

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hunrmand.data.util.DatabaseSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val appModule = module {
    // 0. Database & Local Data Sources
    single { 
        Room.databaseBuilder(
            androidContext(),
            HunrmandDatabase::class.java,
            "hunrmand.db"
        )
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CoroutineScope(Dispatchers.IO).launch {
                    DatabaseSeeder.seed(db)
                }
            }
        })
        .fallbackToDestructiveMigration()
        .build() 
    }
    single { get<HunrmandDatabase>().userDao() }
    single { get<HunrmandDatabase>().jobDao() }
    single { get<HunrmandDatabase>().bidDao() }
    single { get<HunrmandDatabase>().notificationDao() }
    
    single { SessionManager(androidContext()) }
    single<LocalAuthDataSource> { LocalAuthDataSourceImpl(get()) }

    // 1. Repositories (Singletons - Created once)
    single<WorkerRepository> { WorkerRepositoryImpl(get(), get()) }
    single<LocationRepository> { LocationRepositoryImpl(androidContext()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<JobRepository> { JobRepositoryImpl(get()) }
    single<com.example.hunrmand.domain.repository.BidRepository> { com.example.hunrmand.data.repository.BidRepositoryImpl(get()) }
    single<com.example.hunrmand.domain.repository.NotificationRepository> { com.example.hunrmand.data.repository.NotificationRepositoryImpl(get()) }
    
    // UseCases
    single { com.example.hunrmand.domain.usecase.PostJobUseCase(get(), get(), get()) }

    // 2. ViewModels (Created when the screen opens)
    viewModel { MapViewModel(
        locationRepository = get(),
        sessionManager = get()
    ) }  // Injects LocationRepository automatically
    viewModel { HomeViewModel(get(), get()) } // Injects WorkerRepository and SessionManager automatically
    viewModel { SearchViewModel(get()) } // Injects WorkerRepository automatically
    viewModel { com.example.hunrmand.ui.screens.job.JobDetailViewModel(get(), get(), get()) }
    
    // Auth & Jobs
    viewModel { com.example.hunrmand.ui.screens.auth.AuthViewModel(get()) }
    // PostJobViewModel now requires PostJobUseCase, not JobRepository directly (or both, but UseCase covers it)
    viewModel { com.example.hunrmand.ui.screens.job.PostJobViewModel(get(), get()) } 
    viewModel { com.example.hunrmand.ui.screens.profile.ProfileViewModel(get(), get()) }
    viewModel { com.example.hunrmand.ui.screens.booking.BookingViewModel(get(), get()) }
    viewModel { com.example.hunrmand.ui.screens.worker.WorkerHomeViewModel(get(), get(), get()) }
    viewModel { com.example.hunrmand.ui.screens.notification.NotificationViewModel(get(), get()) }
    viewModel { com.example.hunrmand.ui.screens.location.LocationPickerViewModel(get(), get()) }
}