package id.dev.home.presentation.scanResult

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import id.dev.core.presentation.R
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.scanResult.results.ContactResultContent
import id.dev.home.presentation.scanResult.results.GeoResultContent
import id.dev.home.presentation.scanResult.results.LinkResultContent
import id.dev.home.presentation.scanResult.results.PhoneResultContent
import id.dev.home.presentation.scanResult.results.TextResultContent
import id.dev.home.presentation.scanResult.results.WiFiResultContent
import kotlinx.serialization.json.Json
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    navController: NavHostController = rememberNavController(),
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
                        text = stringResource(R.string.scan_result)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
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
            parsedResult?.let { result ->
                when (result) {
                    is BarcodeResult.Link -> {
                        LinkResultContent(
                            modifier = Modifier.align(Alignment.Center),
                            linkResult = result
                        )
                    }

                    is BarcodeResult.Text -> {
                        TextResultContent(
                            modifier = Modifier.align(Alignment.Center),
                            linkResult = result
                        )
                    }
                    is BarcodeResult.Contact -> {
                        ContactResultContent(
                            modifier = Modifier.align(Alignment.Center),
                            linkResult = result
                        )
                    }
                    is BarcodeResult.Geo -> {
                        GeoResultContent(
                            modifier = Modifier.align(Alignment.Center),
                            linkResult = result
                        )
                    }
                    is BarcodeResult.Phone -> {
                        PhoneResultContent(
                            modifier = Modifier.align(Alignment.Center),
                            linkResult = result
                        )
                    }
                    is BarcodeResult.Wifi -> {
                        WiFiResultContent(
                            modifier = Modifier.align(Alignment.Center),
                            linkResult = result
                        )
                    }
                    is BarcodeResult.ScanError -> {}
                }
            }
        }
    }
}