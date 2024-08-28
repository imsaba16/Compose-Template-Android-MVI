package com.example.baseapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.baseapp.domain.repository.ApiRepository
import com.example.baseapp.state.HomeUiIntent
import com.example.baseapp.state.HomeUiState
import com.example.baseapp.utils.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(
    private val repository: ApiRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun processIntent(intent: HomeUiIntent) {
        when (intent) {
            is HomeUiIntent.LoadData -> loadData()
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            repository.fetchData().collect { result ->
                when (result) {
                    is ApiResult.Loading -> _uiState.value = HomeUiState.Loading
                    is ApiResult.Success -> _uiState.value = HomeUiState.Success(result.data)
                    is ApiResult.Error -> _uiState.value = HomeUiState.Error(result.message)
                }
            }
        }
    }
}