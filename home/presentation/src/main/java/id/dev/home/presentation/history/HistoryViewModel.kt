package id.dev.home.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.dev.home.domain.repo.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// dummy code, will be reworked later
class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    val state = MutableStateFlow(TODO())

    fun getAll() { viewModelScope.launch { repository.getAll() } }

    private val _state = state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = getAll()
    )
}