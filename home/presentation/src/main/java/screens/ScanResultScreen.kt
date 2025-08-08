package screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import components.ScanButtons
import id.dev.core.presentation.R
import model.ContentType
import utils.copyToClipboard
import utils.shareLink

@Preview(showBackground = true, showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanResultScreen(
    navController: NavHostController = rememberNavController()
) {
    // this is just dummy code and later will be reworked.
    val ifTypeIsText = ContentType.TEXT.name == "TEXT"
    val ifTypeIsLink = ContentType.LINK.name == "LINK"

    var isExpanded by remember { mutableStateOf(true) }

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
            Box(
                modifier = Modifier.align(Alignment.Center)
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 80.dp),
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
                        // this text stands for Type of the returned value, it could be ->
                        // -> Link, Contact,  Phone Number, Geolocation, Wi-Fi,Text
                        Text(
                            text = "Text",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                        )

                        // this text stands for the value of the returned value
                        Text(
                            text = "Adipiscing ipsum lacinia tincidunt sed. In risus dui accumsan accumsan quam morbi nulla. Dictum justo metus auctor nunc quam id sed. Urna nisi gravida sed lobortis diam pretium. Adipiscing ipsum lacinia tincidunt sed. In risus dui accumsan accumsan quam morbi nulla. Dictum metus auctor nunc quam id sed. Urna nisi gravida sed lobortis diam pretium.",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 20.sp
                            ),
                            modifier = Modifier
                                .padding(bottom = 4.dp)
                                .background(color = if (ifTypeIsLink) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary)
                                .align( alignment = if (ifTypeIsText) Alignment.Start else Alignment.CenterHorizontally)
                        )

                        TextButton(
                            onClick = {
                                // expand logic.
                                isExpanded = !isExpanded
                            },
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(bottom = 24.dp)
                        ) {
                            Text(
                                text = if (isExpanded) stringResource(R.string.show_less) else stringResource(R.string.show_more),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isExpanded) Color(0xFF8C99A2) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Start,
                                ),
                                maxLines = if (isExpanded) Int.MAX_VALUE else 6,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        ActionButtonsLayout()
                    }
                }
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.scan),
                    contentDescription = null,
                    modifier = Modifier
                        .size(160.dp)
                        .align(Alignment.TopCenter)
                        .offset(y = (-80).dp)
                        .background(Color.White, RoundedCornerShape(36.dp))
                )
            }
        }
    }
}

@Composable
private fun ActionButtonsLayout() {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ScanButtons(
            icon = ImageVector.vectorResource(R.drawable.share),
            text = stringResource(R.string.share),
            onClick = {
                shareLink(context, "any") // parse link here.
            },
            modifier = Modifier.weight(1f)
        )
        ScanButtons(
            icon = ImageVector.vectorResource(R.drawable.copy),
            text = stringResource(R.string.copy),
            onClick = {
                context.copyToClipboard("Hello world") // we could take the value from data class directly.
            },
            modifier = Modifier.weight(1f)
        )
    }
}