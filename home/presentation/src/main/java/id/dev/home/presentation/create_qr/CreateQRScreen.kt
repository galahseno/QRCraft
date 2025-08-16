
package id.dev.home.presentation.create_qr

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.core.presentation.theme.QRCraftTheme
import id.dev.core.presentation.utils.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateQRRoot(
    viewModel: CreateQRViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->

    }

    CreateQRScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CreateQRScreen(
    state: CreateQRState,
    onAction: (CreateQRAction) -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Create QR")
    }
}

@Preview
@Composable
private fun Preview() {
    QRCraftTheme {
        CreateQRScreen(
            state = CreateQRState(),
            onAction = {}
        )
    }
}