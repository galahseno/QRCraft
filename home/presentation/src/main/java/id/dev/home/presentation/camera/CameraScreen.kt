package id.dev.home.presentation.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.asFlow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.core.presentation.R
import id.dev.core.presentation.theme.success
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.core.presentation.utils.ObserveAsEvents
import id.dev.home.presentation.camera.component.CameraOverlayWithCutout
import id.dev.home.presentation.camera.component.CameraPermissionHandler
import id.dev.home.presentation.camera.component.CameraPreview
import id.dev.home.presentation.camera.component.dialog.CameraPermissionDialog
import id.dev.home.presentation.camera.component.dialog.LoadingDialog
import id.dev.home.presentation.component.ErrorDialog
import id.dev.home.presentation.utils.checkCameraPermissionAndRationale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun CameraScreenRoot(
    onScanResult: (String) -> Unit,
    viewModel: CameraScreenViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.event) { event ->
        when (event) {
            is CameraScreenEvent.ScanResult -> {
                onScanResult(event.qrId.toString())
            }
        }
    }

    CameraScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    state: CameraScreenState,
    onAction: (CameraScreenAction) -> Unit,
) {
    val view = LocalView.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            onAction(CameraScreenAction.OnImageSelected(uri))
        }
    )

    var cutoutOffset by remember { mutableStateOf(Offset.Zero) }
    var cutoutSizePx by remember { mutableStateOf(IntSize.Zero) }

    // Check initial flashlight capability
    LaunchedEffect(Unit) {
        val hasFlashlightFeature = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        onAction(CameraScreenAction.OnFlashlightAvailabilityChanged(hasFlashlightFeature))
    }

    // Observe torch state changes from camera controller
    LaunchedEffect(state.cameraController) {
        state.cameraController?.cameraInfo?.torchState?.asFlow()?.collect { torchState ->
            onAction(CameraScreenAction.OnTorchStateChanged(torchState))
        }
    }

    // Check actual camera flash capability after camera is bound
    LaunchedEffect(state.cameraController, state.hasCameraPermission) {
        val controller = state.cameraController
        if (controller != null && state.hasCameraPermission) {
            // Wait a bit for camera to fully initialize
            delay(500)
            val hasActualFlash = try {
                controller.cameraInfo?.hasFlashUnit() == true
            } catch (e: Exception) {
                Timber.d("Error checking flash capability: ${e.message}")
                false
            }
            onAction(CameraScreenAction.OnFlashlightAvailabilityChanged(hasActualFlash))
        }
    }

    val scanRect: Rect? = remember(cutoutOffset, cutoutSizePx) {
        if (cutoutSizePx.width > 0 && cutoutSizePx.height > 0) {
            Rect(
                cutoutOffset.x,
                cutoutOffset.y,
                cutoutOffset.x + cutoutSizePx.width,
                cutoutOffset.y + cutoutSizePx.height
            )
        } else null
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
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    if (state.hasFlashlight && state.cameraController != null) {
                        IconButton(
                            onClick = { onAction(CameraScreenAction.OnFlashlightClicked) },
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .background(
                                    color = if (!state.isFlashlightOn) Color.White else Color.Yellow,
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                if (!state.isFlashlightOn) Icons.Outlined.FlashOn else Icons.Outlined.FlashOff,
                                contentDescription = if (!state.isFlashlightOn) stringResource(R.string.off_flashlight) else stringResource(R.string.on_flashlight),
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            ),
                        enabled = !state.isProcessingImage,
                    ) {
                        if (state.isProcessingImage) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.Outlined.Image,
                                contentDescription = null
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
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
                    boundingBox = scanRect,
                    onCameraControllerReady = { controller ->
                        onAction(CameraScreenAction.OnCameraControllerReady(controller))
                    }
                )
            }

            CameraOverlayWithCutout(
                hasCameraPermission = state.hasCameraPermission,
                scanRect = scanRect,
                cutoutSize = when (deviceConfiguration) {
                    DeviceConfiguration.MOBILE_PORTRAIT -> 300.dp
                    DeviceConfiguration.MOBILE_LANDSCAPE -> 200.dp
                    DeviceConfiguration.TABLET_LANDSCAPE -> 400.dp
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

            if (state.isProcessingImage) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(32.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator()
                            Text(
                                text = "Processing Image...",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
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
            ErrorDialog(
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