import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.theme.primary
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.home.presentation.R

@Composable
fun CameraOverlayWithCutout(
    hasCameraPermission: Boolean,
    cutoutOffsetState: Offset,
    cutoutSizeState: IntSize,
    onCutOutOffsetChanged: (Offset) -> Unit,
    onCutOutSizeChanged: (IntSize) -> Unit,
    modifier: Modifier = Modifier,
    cutoutSize: Dp = 300.dp,
    cutoutShape: RoundedCornerShape = RoundedCornerShape(16.dp),
    strokeWidth: Dp = 4.dp,
    cornerLength: Dp = 60.dp,
    overlayColor: Color = Color.Black.copy(alpha = 0.6f)
) {
    val density = LocalDensity.current
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val deviceConfiguration = DeviceConfiguration.fromWindowSizeClass(windowSizeClass)

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cornerRadiusPx = cutoutShape.topStart
                .toPx(Size(cutoutSize.toPx(), cutoutSize.toPx()), density)

            drawRect(color = overlayColor)

            val cutoutPath = Path().apply {
                val scanRect = Rect(
                    cutoutOffsetState.x,
                    cutoutOffsetState.y,
                    cutoutOffsetState.x + cutoutSizeState.width,
                    cutoutOffsetState.y + cutoutSizeState.height
                )

                addRoundRect(
                    RoundRect(
                        rect = scanRect,
                        cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                    )
                )
            }

            if (hasCameraPermission) {
                drawPath(
                    path = cutoutPath,
                    color = Color.Transparent,
                    blendMode = BlendMode.Clear
                )
                val strokeWidthPx = strokeWidth.toPx()
                val cornerLength = cornerLength.toPx()

                // TOP LEFT
                drawArc(
                    color = primary,
                    startAngle = 180f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(cutoutOffsetState.x, cutoutOffsetState.y),
                    size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
                drawLine(
                    color = primary,
                    start = Offset(cutoutOffsetState.x + cornerRadiusPx, cutoutOffsetState.y),
                    end = Offset(cutoutOffsetState.x + cornerLength, cutoutOffsetState.y),
                    strokeWidth = strokeWidthPx
                )
                drawLine(
                    color = primary,
                    start = Offset(cutoutOffsetState.x, cutoutOffsetState.y + cornerRadiusPx),
                    end = Offset(cutoutOffsetState.x, cutoutOffsetState.y + cornerLength),
                    strokeWidth = strokeWidthPx
                )

                // TOP RIGHT
                drawArc(
                    color = primary,
                    startAngle = 270f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(
                        cutoutOffsetState.x + cutoutSize.toPx() - cornerRadiusPx * 2,
                        cutoutOffsetState.y
                    ),
                    size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
                drawLine(
                    color = primary,
                    start = Offset(
                        cutoutOffsetState.x + cutoutSize.toPx() - cornerRadiusPx,
                        cutoutOffsetState.y
                    ),
                    end = Offset(
                        cutoutOffsetState.x + cutoutSize.toPx() - cornerLength,
                        cutoutOffsetState.y
                    ),
                    strokeWidth = strokeWidthPx
                )
                drawLine(
                    color = primary,
                    start = Offset(
                        cutoutOffsetState.x + cutoutSize.toPx(),
                        cutoutOffsetState.y + cornerRadiusPx
                    ),
                    end = Offset(
                        cutoutOffsetState.x + cutoutSize.toPx(),
                        cutoutOffsetState.y + cornerLength
                    ),
                    strokeWidth = strokeWidthPx
                )

                // BOTTOM LEFT
                drawArc(
                    color = primary,
                    startAngle = 90f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(
                        cutoutOffsetState.x,
                        cutoutOffsetState.y + cutoutSize.toPx() - cornerRadiusPx * 2
                    ),
                    size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
                drawLine(
                    color = primary,
                    start = Offset(
                        cutoutOffsetState.x,
                        cutoutOffsetState.y + cutoutSize.toPx() - cornerRadiusPx
                    ),
                    end = Offset(
                        cutoutOffsetState.x,
                        cutoutOffsetState.y + cutoutSize.toPx() - cornerLength
                    ),
                    strokeWidth = strokeWidthPx
                )
                drawLine(
                    color = primary,
                    start = Offset(
                        cutoutOffsetState.x + cornerRadiusPx,
                        cutoutOffsetState.y + cutoutSize.toPx()
                    ),
                    end = Offset(
                        cutoutOffsetState.x + cornerLength,
                        cutoutOffsetState.y + cutoutSize.toPx()
                    ),
                    strokeWidth = strokeWidthPx
                )

                // BOTTOM RIGHT
                drawArc(
                    color = primary,
                    startAngle = 0f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(
                        cutoutOffsetState.x + cutoutSize.toPx() - cornerRadiusPx * 2,
                        cutoutOffsetState.y + cutoutSize.toPx() - cornerRadiusPx * 2
                    ),
                    size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
                drawLine(
                    color = primary,
                    start = Offset(
                        cutoutOffsetState.x + cutoutSize.toPx(),
                        cutoutOffsetState.y + cutoutSize.toPx() - cornerRadiusPx
                    ),
                    end = Offset(
                        cutoutOffsetState.x + cutoutSize.toPx(),
                        cutoutOffsetState.y + cutoutSize.toPx() - cornerLength
                    ),
                    strokeWidth = strokeWidthPx
                )
                drawLine(
                    color = primary,
                    start = Offset(
                        cutoutOffsetState.x + cutoutSize.toPx() - cornerRadiusPx,
                        cutoutOffsetState.y + cutoutSize.toPx()
                    ),
                    end = Offset(
                        cutoutOffsetState.x + cutoutSize.toPx() - cornerLength,
                        cutoutOffsetState.y + cutoutSize.toPx()
                    ),
                    strokeWidth = strokeWidthPx
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                16.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.point_camera_qr),
                color = Color.White,
                style = when (deviceConfiguration) {
                    DeviceConfiguration.MOBILE_PORTRAIT, DeviceConfiguration.MOBILE_LANDSCAPE -> MaterialTheme.typography.titleSmall
                    else -> MaterialTheme.typography.titleMedium
                }
            )

            Box(
                modifier = Modifier
                    .size(cutoutSize)
                    .clip(cutoutShape)
                    .onGloballyPositioned { coords ->
                        onCutOutOffsetChanged(coords.positionInParent())
                        onCutOutSizeChanged(coords.size)
                    }
            ) {
                if (!hasCameraPermission) {
                    Image(
                        painter = painterResource(id = R.drawable.qr_sample),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.matchParentSize()
                    )
                }
            }
        }
    }

}
