package com.example.baseapp.repository

import com.example.baseapp.model.BaseModel

interface HomeRepository {
    suspend fun getFact() : Result<BaseModel>
}