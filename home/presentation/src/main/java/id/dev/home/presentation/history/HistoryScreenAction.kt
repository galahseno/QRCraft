package id.dev.home.presentation.history

import id.dev.home.domain.QrItem
import id.dev.home.presentation.model.ScanHistoryTab

sealed interface HistoryScreenAction {
    data class OnTabSelected(val tab: ScanHistoryTab) : HistoryScreenAction
    data object OnDeleteClicked : HistoryScreenAction

    data class OnTopBarCollapsed(val isCollapsed: Boolean) : HistoryScreenAction
    data class OnItemClick(val qrId: String): HistoryScreenAction
    data class OnItemLongClick(val qrItem: QrItem): HistoryScreenAction
    data object OnModalBottomSheetDismiss : HistoryScreenAction
}