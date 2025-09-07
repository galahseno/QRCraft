package id.dev.home.presentation.history

import id.dev.home.domain.QrItem
import id.dev.home.presentation.model.ScanHistoryTab

data class HistoryScreenState(
    val selectedTab: ScanHistoryTab = ScanHistoryTab.Scanned,
    val scannedHistory: List<QrItem> = emptyList(),
    val generatedHistory: List<QrItem> = emptyList(),
    val displayBottomSheet: Boolean = false,
    val selectedQrItem: QrItem? = null
)