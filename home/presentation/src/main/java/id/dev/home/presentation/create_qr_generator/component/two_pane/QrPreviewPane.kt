package id.dev.home.presentation.create_qr_generator.component.two_pane

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.dev.home.presentation.create_qr_generator.GenerateQrScreenState
import id.dev.home.presentation.create_qr_generator.component.EmptyPreview
import id.dev.home.presentation.model.QrTypeIdentifier

@Composable
internal fun QrPreviewPane(
    state: GenerateQrScreenState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.generatedQrCode != null) {
            state.qrContent?.let {
                LandscapePreviewUI(
                    qrCode = it
                )
            }
        } else {
            when (state.qrTypeIdentifier) {
                QrTypeIdentifier.TEXT -> EmptyPreview(message = "Empty Text")
                QrTypeIdentifier.LINK -> {
                    EmptyPreview(message = "Enter URL to preview")
                }

                QrTypeIdentifier.CONTACT -> EmptyPreview(message = "Enter contact details to preview")
                QrTypeIdentifier.PHONE -> EmptyPreview(message = "Enter phone to preview")
                QrTypeIdentifier.GEO -> EmptyPreview(message = "Enter coordinates to preview")
                QrTypeIdentifier.WIFI -> EmptyPreview(message = "Enter WiFi details to preview")
            }
        }
    }
}