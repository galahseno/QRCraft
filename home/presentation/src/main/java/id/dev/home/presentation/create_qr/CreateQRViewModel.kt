package id.dev.home.presentation.create_qr

import androidx.lifecycle.ViewModel
import id.dev.home.presentation.create_qr.model.QRType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreateQRViewModel : ViewModel() {

    private val _state = MutableStateFlow(CreateQRState(QRType.entries))
    val state = _state.asStateFlow()

    fun onAction(action: CreateQRAction) {

    }
}