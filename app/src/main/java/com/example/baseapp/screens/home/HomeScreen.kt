package com.example.baseapp.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.*
import com.example.baseapp.utils.ToastStateManager
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
private fun Preview() {
    HomeScreenContent(HomeState())
}

@Composable
fun HomeScreen(homeViewModel: HomeViewModel = koinViewModel()) {
    val state by homeViewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        homeViewModel.sideEffect?.collect { sideEffect ->
            when (sideEffect) {
                is HomeSideEffect.ShowMessage -> ToastStateManager.showToast(sideEffect.message)
            }
        }
    }
    HomeScreenContent(state) {
        homeViewModel.onIntent(it)
    }
}

@Composable
private fun HomeScreenContent(state: HomeState, intent: (HomeIntent) -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (state.isLoading) {
            Text("Loading...")
        } else {
            Text("---Fact---\n\n${state.fact}", modifier = Modifier.clickable {
                intent(HomeIntent.Load)
            })
        }
    }
}