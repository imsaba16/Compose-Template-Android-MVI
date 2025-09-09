package com.example.baseapp.repository

import com.example.baseapp.model.BaseModel
import com.example.baseapp.service.HomeService

class HomeRepositoryImpl(private val homeService: HomeService): HomeRepository {
    override suspend fun getFact(): Result<BaseModel> = homeService.getFact()
}