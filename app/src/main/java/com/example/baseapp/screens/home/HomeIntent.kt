package com.example.baseapp.screens.home

sealed class HomeIntent {
    object Load: HomeIntent()
}

sealed class HomeSideEffect {
    data class ShowMessage(val message: String): HomeSideEffect()
}