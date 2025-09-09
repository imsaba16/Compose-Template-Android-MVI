package com.example.baseapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

abstract class BaseViewModel<S, T>(initialState: S, enableSideEffect: Boolean = false) : ViewModel() {
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> get() = _state

    private val _sideEffect: Channel<T>? = if (enableSideEffect) Channel() else null
    val sideEffect = _sideEffect?.receiveAsFlow()

    protected fun reduce(update: S.() -> S) {
        _state.value = _state.value.update()
    }

    protected suspend fun postSideEffect(effect: T) {
        _sideEffect?.send(effect)
    }

    protected fun launchDefault(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.Default) { block() }
    }

    protected fun launchIO(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) { block() }
    }

    protected fun launchMain(block: suspend () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) { block() }
    }

    protected fun <T> stateProperty(property: (S) -> T): ReadOnlyProperty<BaseViewModel<S, T>, T> {
        return object : ReadOnlyProperty<BaseViewModel<S, T>, T> {
            override fun getValue(thisRef: BaseViewModel<S, T>, property: KProperty<*>): T {
                return property(thisRef.state.value)
            }
        }
    }
}