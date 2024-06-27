package com.example.demo

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(): BlocViewModel<Event, State>() {

    private val textFlow = MutableSharedFlow<String>()
    private val textFlow2 = MutableSharedFlow<String>()


    override val _uiState: StateFlow<State> = combine(
        textFlow.onStart { emit("") },
        textFlow2.onStart { emit("") }
    ) { text1, text2 ->

        State(text1, text2)


    }.stateIn(
        scope = viewModelScope,
        initialValue = State(text1="", text2 = ""),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000)
    )


    init {

        on(Event.TextChanged::class) {
            textFlow.emit(it.text)
        }
    }
}

sealed interface Event {
    data class TextChanged(val text: String): Event
}

data class State(
    val text1: String,
    val text2: String,
)