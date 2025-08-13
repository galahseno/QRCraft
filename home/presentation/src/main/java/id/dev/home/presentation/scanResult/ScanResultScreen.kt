package id.dev.home.presentation.scanResult

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.dev.core.presentation.R
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.core.presentation.utils.ObserveAsEvents
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.scanResult.components.QrCodeImageLayout
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@Composable
fun ScanResultScreenRoot(
    barcodeResult: String,
    onNavigateUp: () -> Unit = {},
    onOpenLink: (String) -> Unit = {},
    onShare: (String) -> Unit = {},
    onCopyClicked: (String) -> Unit = {},
    viewModel: ScanResultScreenViewModel = koinViewModel()
) {

    ObserveAsEvents(viewModel.event) {
        when (it) {
            ScanResultScreenEvent.NavigateUp -> onNavigateUp()
            is ScanResultScreenEvent.OpenLink -> onOpenLink(it.url)
            is ScanResultScreenEvent.ShareContent -> onShare(it.share)
            is ScanResultScreenEvent.CopyContent -> onCopyClicked(it.copy)
        }
    }

    ScanResultScreen(
        onAction = viewModel::onAction,
        barcodeResult = barcodeResult
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    onAction: (ScanResultScreenAction) -> Unit,
    barcodeResult: String
) {
    val parsedResult = remember(barcodeResult) {
        try {
            if (barcodeResult.isNotEmpty()) {
                Json.decodeFromString<BarcodeResult>(barcodeResult)
            } else {
                null
            }
        } catch (e: Exception) {
            Timber.tag("ScanResultScreen").e(e, "Failed to parse barcode result")
            null
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.statusBars,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.scan_result),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onAction(ScanResultScreenAction.OnNavigateUpClicked) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.navigate_up),
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                )
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onSurface)
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(WindowInsets.navigationBars)
        ) {
            when (parsedResult) {
                // DONE
                is BarcodeResult.Link -> {
                    ScanResultCard(
                        modifier = Modifier.align(Alignment.Center),
                        result = parsedResult,
                        onAction = onAction,
                        resultText = parsedResult.url,
                        contentText = {
                            Text(
                                text = parsedResult.url,
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
                                        onAction(ScanResultScreenAction.OnLinkClicked(link = parsedResult.url))
                                    }
                            )
                        }
                    )
                }
                // DONE
                is BarcodeResult.Text -> {
                    var expanded by remember { mutableStateOf(false) }
                    ScanResultCard(
                        modifier = Modifier.align(Alignment.Center),
                        result = parsedResult,
                        onAction = onAction,
                        resultText = parsedResult.content,
                        contentText = {
                            Text(
                                text = parsedResult.content,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                ),
                                modifier = Modifier
                                    .width(480.dp)
                                    .padding(bottom = 4.dp)
                                    .align(alignment = Alignment.TopStart),
                                overflow = TextOverflow.Ellipsis,
                                maxLines = if (expanded) Int.MAX_VALUE else 6
                            )
                        },
                        collapsibleTextButton = {
                            Text(
                                text = if (!expanded) stringResource(R.string.show_more) else stringResource(R.string.show_less),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    color = if (!expanded) MaterialTheme.colorScheme.onSurfaceVariant else Color(0xFF8C99A2)
                                ),
                                modifier = Modifier.fillMaxWidth().align(Alignment.TopStart).clickable {
                                    expanded = !expanded
                                }
                            )
                        }
                    )
                }
                // DONE
                is BarcodeResult.Contact -> {
                    ScanResultCard(
                        modifier = Modifier.align(Alignment.Center),
                        result = parsedResult,
                        onAction = onAction,
                        resultText = "${parsedResult::class.java.simpleName}: \n Name → ${parsedResult.name} \n Email → ${parsedResult.email} \n Phone number → ${parsedResult.phone}",
                        contentText = {
                            Column {
                                Text(
                                    text = parsedResult.name,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = parsedResult.email,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = parsedResult.phone,
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
                // DONE
                is BarcodeResult.Geo -> {
                    ScanResultCard(
                        modifier = Modifier.align(Alignment.Center),
                        result = parsedResult,
                        onAction = onAction,
                        resultText = "${parsedResult::class.java.simpleName}: \n Latitude → ${parsedResult.lat} \n Longitude → ${parsedResult.lng}",
                        contentText = {
                            Text(
                                text = parsedResult.lat.toString() + "," + parsedResult.lng.toString(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                ),
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .align(alignment = Alignment.Center)
                            )
                        }
                    )
                }
                // DONE
                is BarcodeResult.Phone -> {
                    ScanResultCard(
                        modifier = Modifier.align(Alignment.Center),
                        result = parsedResult,
                        onAction = onAction,
                        resultText = parsedResult.number,
                        contentText = {
                            Text(
                                text = parsedResult.number,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                ),
                                modifier = Modifier
                                    .padding(bottom = 4.dp)
                                    .align(alignment = Alignment.Center)
                            )
                        }
                    )
                }
                // DONE
                is BarcodeResult.Wifi -> {
                    ScanResultCard(
                        modifier = Modifier.align(Alignment.Center),
                        result = parsedResult,
                        onAction = onAction,
                        resultText = parsedResult.ssid,
                        contentText = {
                            Column {
                                Text(
                                    text = parsedResult.ssid,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )

                                Text(
                                    text = parsedResult.password,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)

                                        .align(alignment = Alignment.CenterHorizontally)
                                )

                                Text(
                                    text = parsedResult.encryptionType,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    modifier = Modifier
                                        .padding(bottom = 4.dp)
                                        .align(alignment = Alignment.CenterHorizontally)
                                )
                            }
                        }
                    )
                }
                is BarcodeResult.ScanError -> {}
                null -> {}
            }
        }
    }
}
@Composable
fun ScanResultCard(
    modifier: Modifier = Modifier,
    result: BarcodeResult,
    onAction: (ScanResultScreenAction) -> Unit,
    resultText: String,
    contentText: (@Composable (() -> Unit))? = null,
    collapsibleTextButton: (@Composable (() -> Unit))? = null
) {

    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val cardWidth = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 380.dp
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> 480.dp
        else -> 380.dp
    }

    Box(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.width(cardWidth),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
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
                    onAction = onAction
                )
            }
        }
        QrCodeImageLayout()
    }
}