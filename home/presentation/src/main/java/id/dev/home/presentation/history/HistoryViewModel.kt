package id.dev.home.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.home.domain.HistoryRepository
import id.dev.home.presentation.model.ScanHistoryTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: HistoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryScreenState())
    val state = _state.asStateFlow()

    init {
        repository
            .observeQrItemsBySource(ScanHistoryTab.Scanned.name)
            .distinctUntilChanged()
            .onEach { qrItems ->
                _state.update {
                    it.copy(
                        scannedHistory = qrItems
                    )
                }
            }
            .launchIn(viewModelScope)

        repository
            .observeQrItemsBySource(ScanHistoryTab.Generated.name)
            .distinctUntilChanged()
            .onEach { qrItems ->
                _state.update {
                    it.copy(
                        generatedHistory = qrItems
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: HistoryScreenAction) {
        when (action) {
            is HistoryScreenAction.OnTabSelected -> {
                _state.update {
                    it.copy(selectedTab = action.tab)
                }
            }

            is HistoryScreenAction.OnItemLongClick -> {
                _state.update {
                    it.copy(
                        displayBottomSheet = true,
                        selectedQrItem = action.qrItem
                    )
                }
            }

            is HistoryScreenAction.OnModalBottomSheetDismiss -> {
                _state.update {
                    it.copy(
                        displayBottomSheet = false,
                        selectedQrItem = null
                    )
                }
            }

            is HistoryScreenAction.OnDeleteClicked -> {
                viewModelScope.launch {
                    _state.value.selectedQrItem?.let {
                        it.id?.let { id ->
                            repository.deleteQrItem(id)
                        }
                    }

                    _state.update {
                        it.copy(
                            displayBottomSheet = false,
                            selectedQrItem = null
                        )
                    }
                }
            }

            is HistoryScreenAction.OnFavoriteClick -> {
                action.qrItem.id?.let {
                    viewModelScope.launch {
                        repository.updateFavoriteById(
                            id = it,
                            isFavorite = !action.qrItem.isFavorite
                        )
                    }

                }
            }

            else -> Unit
        }
    }
}