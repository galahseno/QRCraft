package id.dev.home.presentation.scan_result.component

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import id.dev.core.presentation.R
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.core.presentation.utils.applyIf
import id.dev.home.presentation.component.QrCodeImageLayout
import id.dev.home.presentation.model.QrTypeIdentifier

@Composable
internal fun ScanResultCard(
    qrTypes: QrTypeIdentifier,
    qrTitle: String,
    onTitleChanged: (String) -> Unit,
    content: String,
    modifier: Modifier = Modifier,
    onSaveClicked: () -> Unit = {},
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    val cardWidth = when (deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT -> 380.dp
        DeviceConfiguration.TABLET_PORTRAIT, DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> 480.dp

        else -> 380.dp
    }
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var needsToggle by remember { mutableStateOf(false) }
    val focusTitleRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(modifier = Modifier.height(100.dp))

                BasicTextField(
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                            focusTitleRequester.freeFocus()
                        }
                    ),
                    modifier = modifier
                        .fillMaxWidth()
                        .focusRequester(focusTitleRequester),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    value = qrTitle,
                    onValueChange = {
                        if (it.length <= 32) {
                            onTitleChanged(it)
                        }
                    },
                    decorationBox = { innerTextField ->
                        if (qrTitle.isEmpty()) {
                            Text(
                                text = qrTypes.name.lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                        innerTextField()
                    },
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                )

                Text(
                    modifier = Modifier
                        .applyIf(qrTypes == QrTypeIdentifier.LINK) {
                            background(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                            )
                        }
                        .padding(4.dp)
                        .animateContentSize()
                        .applyIf(qrTypes == QrTypeIdentifier.LINK) {
                            clickable {
                                val intent =
                                    Intent(Intent.ACTION_VIEW, content.toUri())
                                context.startActivity(intent)
                            }
                        },
                    textAlign = TextAlign.Center,
                    text = content,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (expanded) Int.MAX_VALUE else 6,
                    onTextLayout = { result ->
                        if (!expanded) {
                            needsToggle = result.lineCount > 6 || result.didOverflowHeight
                        }
                    }
                )

                if (needsToggle) {
                    Text(
                        text = if (!expanded) stringResource(R.string.show_more)
                        else stringResource(R.string.show_less),
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = if (!expanded) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.inverseOnSurface
                        ),
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = null, indication = null
                            ) {
                                expanded = !expanded
                            },
                    )
                }

                ActionButtonsLayout(
                    share = content,
                    copyToClipboard = content,
                    onSaveClicked = onSaveClicked
                )
            }
        }

        QrCodeImageLayout(
            text = content,
        )
    }
}