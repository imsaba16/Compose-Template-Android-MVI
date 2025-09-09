package com.example.baseapp.screens.home

import com.example.baseapp.repository.HomeRepository
import com.example.baseapp.utils.BaseViewModel
import com.example.baseapp.utils.result

class HomeViewModel(private val homeRepository: HomeRepository) : BaseViewModel<HomeState, HomeSideEffect>(HomeState(), true) {
    init {
        onIntent(HomeIntent.Load)
    }
    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.Load -> launchIO {
                reduce { copy(isLoading = true) }
                homeRepository.getFact()
                    .result(
                        onSuccess = {
                            reduce { copy(fact = it.fact) }
                        },
                        onFailure = { code, message ->
                            postSideEffect(HomeSideEffect.ShowMessage(message))
                        }
                    )
                reduce { copy(isLoading = false) }
            }
        }
    }
}