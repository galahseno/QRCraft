package id.dev.home.presentation.scanResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ScanResultScreenViewModel: ViewModel() {

    private val _event = MutableSharedFlow<ScanResultScreenEvent>()
    val event = _event.asSharedFlow()

    fun onAction(action: ScanResultScreenAction) {
        when (action) {
            ScanResultScreenAction.OnNavigateUpClicked -> {
                viewModelScope.launch {
                    _event.emit(ScanResultScreenEvent.NavigateUp)
                }
            }
            is ScanResultScreenAction.OnLinkClicked -> handleLinkClicked(action.link)
            is ScanResultScreenAction.OnShareClicked -> handleShareClicked(action.shareContent)
            is ScanResultScreenAction.OnCopyClicked -> handleCopyClicked(action.copyContent)
        }
    }

    private fun handleLinkClicked(link: String) {
        viewModelScope.launch {
            _event.emit(ScanResultScreenEvent.OpenLink(link))
        }
    }

    private fun handleCopyClicked(copyContent: String) {
        viewModelScope.launch {
            _event.emit(ScanResultScreenEvent.CopyContent(copyContent))
        }
    }

    private fun handleShareClicked(shareContent: String) {
        viewModelScope.launch {
            _event.emit(ScanResultScreenEvent.ShareContent(shareContent))
        }
    }
}