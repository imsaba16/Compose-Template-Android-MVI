package com.example.baseapp.di

import android.util.Log
import com.example.baseapp.service.ApiService
import com.example.baseapp.service.HomeService
import io.ktor.client.*
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val NETWORK_TIME_OUT = 30_000L

val networkModule = module {
    single<Lazy<HttpClient>> {
        lazy {
            HttpClient(Android) {
                install(ContentNegotiation) {
                    json(Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                        encodeDefaults = false
                    })
                }

                install(HttpTimeout) {
                    requestTimeoutMillis = NETWORK_TIME_OUT
                    connectTimeoutMillis = NETWORK_TIME_OUT
                    socketTimeoutMillis = NETWORK_TIME_OUT
                }

                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Log.v("Logger Ktor =>", message)
                        }
                    }
                    level = LogLevel.BODY
                }

                install(DefaultRequest) {
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                }

                defaultRequest {
                    contentType(ContentType.Application.Json)
                    accept(ContentType.Application.Json)
                }
                HttpResponseValidator {
                    handleResponseExceptionWithRequest { cause, _ ->
                        when (cause) {
                            is java.net.UnknownHostException -> {
                                throw Exception("No internet connection. Please check your network.")
                            }
                            is java.net.SocketTimeoutException -> {
                                throw Exception("Request timed out. Please try again.")
                            }
                            is HttpRequestTimeoutException -> {
                                throw Exception("Request took too long. Please retry.")
                            }
                        }
                    }
                }
            }
        }
    }

    factory { ApiService(get(), get()) }
    factory { HomeService(get()) }
}