package com.example.baseapp.state

import com.example.baseapp.domain.model.BaseModel

sealed class HomeUiState {
    object Loading: HomeUiState()
    data class Success(val data: List<BaseModel>): HomeUiState()
    data class Error(val message: String): HomeUiState()
}