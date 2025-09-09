package com.example.baseapp.di

import com.example.baseapp.repository.HomeRepository
import com.example.baseapp.repository.HomeRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory<HomeRepository> { HomeRepositoryImpl(get()) }
}