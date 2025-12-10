package com.example.hunrmand

import android.app.Application
import com.example.hunrmand.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HunrmandApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HunrmandApp)
            modules(appModule)
        }
    }
}