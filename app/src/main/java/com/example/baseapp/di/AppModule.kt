package com.example.baseapp.di

import com.example.baseapp.utils.PreferenceManager
import org.koin.dsl.module

val appModule = module {
    single { PreferenceManager(get()) }
}