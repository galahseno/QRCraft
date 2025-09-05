package id.dev.home.presentation.history

sealed interface HistoryScreenAction {
    data object OnDeleteClicked : HistoryScreenAction
    data object OnItemSelected: HistoryScreenAction
}