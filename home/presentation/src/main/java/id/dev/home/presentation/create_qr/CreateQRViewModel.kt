package id.dev.home.presentation.create_qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CreateQRViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(CreateQRState(availableTypes))
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = CreateQRState()
        )

    private val _event = Channel<CreateQREvent>()
    val event = _event.receiveAsFlow()

    fun onAction(action: CreateQRAction) {
        viewModelScope.launch {
            when (action) {
                is CreateQRAction.SelectQRType -> {
                    _event.send(CreateQREvent.NavigateToQRGenerator(action.typeIdentifier))
                }
            }
        }
    }
}