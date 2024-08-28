package com.example.baseapp.di.api

import com.example.baseapp.domain.model.BaseModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("/test")
    suspend fun fetchData(): Response<List<BaseModel>>
}
