package com.example.baseapp.app

import android.app.Application
import com.example.baseapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule, networkModule, repositoryModule, viewModelModule)
        }
    }
}