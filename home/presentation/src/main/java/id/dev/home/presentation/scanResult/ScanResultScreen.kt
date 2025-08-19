package id.dev.home.presentation.scanResult

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import id.dev.core.presentation.R
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.scanResult.components.ActionButtonsLayout
import id.dev.home.presentation.scanResult.components.QrCodeImageLayout
import id.dev.home.presentation.scanResult.components.ScanResultTopBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ScanResultScreenRoot(
    onNavigateUp: () -> Unit = {},
    viewModel: ScanResultScreenViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScanResultScreen(
        barcodeResult = state.barcodeResult, onAction = { action ->
            when (action) {
                ScanResultScreenAction.OnNavigateUpClicked -> onNavigateUp()
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ScanResultScreen(
    barcodeResult: BarcodeResult?,
    onAction: (ScanResultScreenAction) -> Unit,
) {
    val context = LocalContext.current
    val view = LocalView.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ScanResultTopBar(
                onBackClick = {
                    onAction(ScanResultScreenAction.OnNavigateUpClicked)
                }
            )
        }
    ) { innerPadding ->
        if (barcodeResult == null) return@Scaffold

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onSurface)
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            contentAlignment = Alignment.TopCenter
        ) {
            when (barcodeResult) {
                is BarcodeResult.Link -> {
                    ScanResultCard(
                        result = barcodeResult, resultText = barcodeResult.url, contentText = {
                            Text(
                                text = barcodeResult.url,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                ),
                                modifier = Modifier
                                    .padding(all = 16.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    )
                                    .padding(all = 4.dp)
                                    .clickable {
                                        val intent =
                                            Intent(Intent.ACTION_VIEW, barcodeResult.url.toUri())
                                        context.startActivity(intent)
                                    })
                        })
                }

                is BarcodeResult.Text -> {
                    var expanded by remember { mutableStateOf(false) }

                    ScanResultCard(
                        result = barcodeResult,
                        resultText = barcodeResult.content,
                        contentText = {
                            Text(
                                text = barcodeResult.content,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp
                                ),
                                modifier = Modifier
                                    .width(480.dp)
                                    .animateContentSize()
                                    .padding(bottom = 4.dp)
                                    .align(alignment = Alignment.TopStart),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = if (expanded) Int.MAX_VALUE else 6
                            )
                        },
                        collapsibleTextButton = {
                            Text(
                                text = if (!expanded) stringResource(R.string.show_more) else stringResource(
                                    R.string.show_less
                                ),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = if (!expanded) MaterialTheme.colorScheme.onSurfaceVariant else Color(
                                        0xFF8C99A2
                                    )
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.TopStart)
                                    .clickable(
                                        interactionSource = null, indication = null
                                    ) {
                                        expanded = !expanded
                                    })
                        })
                }

                is BarcodeResult.Contact -> {
                    ScanResultCard(
                        result = barcodeResult,
                        resultText = "${barcodeResult::class.java.simpleName}: \n Name → ${barcodeResult.name} \n Email → ${barcodeResult.email} \n Phone number → ${barcodeResult.phone}",
                        contentText = {
                            Column {
                                Text(
                                    text = barcodeResult.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = barcodeResult.email,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = barcodeResult.phone,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )
                            }
                        },
                    )
                }

                is BarcodeResult.Geo -> {
                    ScanResultCard(
                        result = barcodeResult,
                        resultText = "${barcodeResult::class.java.simpleName}: \n Latitude → ${barcodeResult.lat} \n Longitude → ${barcodeResult.lng}",
                        contentText = {
                            Text(
                                text = barcodeResult.lat.toString() + "," + barcodeResult.lng.toString(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp
                                ),
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .align(alignment = Alignment.Center)
                            )
                        })
                }

                is BarcodeResult.Phone -> {
                    ScanResultCard(
                        result = barcodeResult, resultText = barcodeResult.number, contentText = {
                            Text(
                                text = barcodeResult.number,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface, lineHeight = 20.sp
                                ),
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .align(alignment = Alignment.Center)
                            )
                        })
                }

                is BarcodeResult.Wifi -> {
                    ScanResultCard(
                        result = barcodeResult, resultText = barcodeResult.ssid, contentText = {
                            Column {
                                Text(
                                    text = "SSID: ${barcodeResult.ssid}",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )

                                Text(
                                    text = "Password: ${barcodeResult.password}",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)

                                        .align(alignment = Alignment.CenterHorizontally)
                                )

                                Text(
                                    text = "Encryption type: ${barcodeResult.encryptionType}",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )
                            }
                        })
                }

                is BarcodeResult.ScanError -> {}
            }
        }
    }

    SideEffect {
        val window = (context as? Activity)?.window
        if (!view.isInEditMode && window != null) {
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
                false
        }
    }
}

@Composable
fun ScanResultCard(
    modifier: Modifier = Modifier,
    result: BarcodeResult,
    resultText: String,
    contentText: (@Composable (() -> Unit))? = null,
    collapsibleTextButton: (@Composable (() -> Unit))? = null
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val cardWidth = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 380.dp
        DeviceConfiguration.TABLET_PORTRAIT, DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> 480.dp

        else -> 380.dp
    }

    Box(
        modifier = modifier
            .padding(top = 32.dp)
    ) {
        Card(
            modifier = Modifier
                .width(cardWidth)
                .padding(top = 80.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                Text(
                    text = result::class.java.simpleName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                if (contentText != null) {
                    contentText()
                    Spacer(Modifier.width(8.dp))
                }

                if (collapsibleTextButton != null) {
                    collapsibleTextButton()
                    Spacer(Modifier.width(8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                ActionButtonsLayout(
                    share = "Link: $resultText",
                    copyToClipboard = resultText,
                )
            }
        }

        QrCodeImageLayout(
            text = resultText,
        )
    }
}