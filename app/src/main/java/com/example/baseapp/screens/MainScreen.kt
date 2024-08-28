package com.example.baseapp.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.baseapp.state.HomeUiIntent
import com.example.baseapp.state.HomeUiState
import com.example.baseapp.viewmodel.ApiViewModel

@Composable
fun MainScreen(innerPadding: PaddingValues, viewModel: ApiViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    when (uiState) {
        is HomeUiState.Loading -> println("Loading")
        is HomeUiState.Success -> println("Success")
        is HomeUiState.Error -> println((uiState as HomeUiState.Error).message)
    }

    viewModel.processIntent(HomeUiIntent.LoadData)

}

@Preview
@Composable
fun MainPreview() {
    val innerPadding = PaddingValues()
    MainScreen(innerPadding = innerPadding)
}