package com.example.baseapp.domain.repository

import com.example.baseapp.di.api.ApiService
import com.example.baseapp.domain.model.BaseModel
import com.example.baseapp.utils.ApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(private val apiService: ApiService) {

    fun fetchData(): Flow<ApiResult<List<BaseModel>>> = flow {
        emit(ApiResult.Loading)
        try {
            val response = apiService.fetchData()
            if (response.isSuccessful) {
                emit(ApiResult.Success(response.body() ?: emptyList()))
            } else {
                emit(ApiResult.Error("Network call failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error("Network call failed: ${e.message}"))
        }
    }
}
