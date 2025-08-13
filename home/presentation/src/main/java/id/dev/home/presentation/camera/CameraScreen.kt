package id.dev.home.presentation.camera

import CameraOverlayWithCutout
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowWidthSizeClass
import id.dev.core.presentation.theme.QRCraftTheme
import id.dev.core.presentation.theme.success
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.core.presentation.utils.ObserveAsEvents
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.camera.component.dialog.CameraPermissionDialog
import id.dev.home.presentation.camera.component.CameraPermissionHandler
import id.dev.home.presentation.camera.component.CameraPreview
import id.dev.home.presentation.camera.component.dialog.LoadingDialog
import id.dev.home.presentation.camera.component.dialog.ScanErrorDialog
import id.dev.home.presentation.utils.checkCameraPermissionAndRationale
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CameraScreenRoot(
    onScanResult: (BarcodeResult) -> Unit,
    viewModel: CameraScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            is CameraScreenEvent.ScanResult -> {
                onScanResult(event.result)
            }
        }
    }

    CameraScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CameraScreen(
    state: CameraScreenState,
    onAction: (CameraScreenAction) -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var cutoutOffset by remember { mutableStateOf(Offset.Zero) }
    var cutoutSizePx by remember { mutableStateOf(IntSize.Zero) }

    val boundingBox: Rect? = remember(cutoutSizePx, cutoutSizePx) {
        if (cutoutSizePx.width > 0 && cutoutSizePx.height > 0) {
            Rect(
                cutoutOffset.x,
                cutoutOffset.y,
                cutoutOffset.x + cutoutSizePx.width,
                cutoutOffset.y + cutoutSizePx.height
            )
        } else {
            null
        }
    }

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(id.dev.home.presentation.R.string.camera_permission_granted),
                    duration = SnackbarDuration.Short
                )
            }
        }
        val activity = context as ComponentActivity
        activity.checkCameraPermissionAndRationale(onAction)
    }

    CameraPermissionHandler(onAction = onAction)

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        modifier = Modifier.fillMaxWidth(0.65f),
                        containerColor = success,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                alignment = Alignment.CenterHorizontally
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = data.visuals.message,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(it)
        ) {
            if (state.hasCameraPermission) {
                CameraPreview(
                    onQrCodeScanned = { result ->
                        onAction(CameraScreenAction.OnScanResult(result))
                    },
                    boundingBox = boundingBox
                )
            }
            CameraOverlayWithCutout(
                hasCameraPermission = state.hasCameraPermission,
                cutoutOffsetState = cutoutOffset,
                cutoutSizeState = cutoutSizePx,
                cutoutSize = when (deviceConfiguration) {
                    DeviceConfiguration.MOBILE_PORTRAIT, DeviceConfiguration.MOBILE_LANDSCAPE -> 300.dp
                    else -> 500.dp
                },
                cutoutShape = when (deviceConfiguration) {
                    DeviceConfiguration.MOBILE_PORTRAIT, DeviceConfiguration.MOBILE_LANDSCAPE -> RoundedCornerShape(
                        16.dp
                    )

                    else -> RoundedCornerShape(32.dp)
                },
                strokeWidth = when (deviceConfiguration) {
                    DeviceConfiguration.MOBILE_PORTRAIT, DeviceConfiguration.MOBILE_LANDSCAPE -> 4.dp
                    else -> 8.dp
                },
                cornerLength = when (deviceConfiguration) {
                    DeviceConfiguration.MOBILE_PORTRAIT, DeviceConfiguration.MOBILE_LANDSCAPE -> 60.dp
                    else -> 120.dp
                },
                onCutOutSizeChanged = { size ->
                    cutoutSizePx = size
                },
                onCutOutOffsetChanged = { offset ->
                    cutoutOffset = offset
                },
            )
        }
    }

    when {
        !state.hasCameraPermission || state.showScreenRationale -> {
            CameraPermissionDialog(
                onCloseClick = {
                    (context as ComponentActivity).finishAffinity()
                },
                onGrantAccessClick = {
                    if (state.showScreenRationale) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }
            )
        }

        state.isScanError && state.errorMessage != null -> {
            ScanErrorDialog(
                errorMessage = state.errorMessage.asString(),
                onDismissRequest = {
                    onAction(CameraScreenAction.OnDismissErrorDialog)
                }

            )
        }

        state.isLoading -> LoadingDialog()
    }

    SideEffect {
        val window = (context as? Activity)?.window
        if (!view.isInEditMode && window != null) {
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                !state.hasCameraPermission
        }
    }
}

@Preview
@Composable
private fun Preview() {
    QRCraftTheme {
        CameraScreen(
            state = CameraScreenState(),
            onAction = {}
        )
    }
}