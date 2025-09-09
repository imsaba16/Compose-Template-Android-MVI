package com.example.baseapp.service

import com.example.baseapp.model.BaseModel
import com.example.baseapp.utils.safeApiCall

class HomeService(val apiService: ApiService) {
    companion object {
        private const val FACT = "/fact"
    }

    suspend fun getFact() : Result<BaseModel> =
        safeApiCall {
            apiService.get(path = FACT)
        }
}