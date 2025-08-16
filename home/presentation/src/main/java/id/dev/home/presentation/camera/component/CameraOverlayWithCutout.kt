import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.theme.primary
import id.dev.core.presentation.utils.DeviceConfiguration
import id.dev.home.presentation.R

@Composable
fun CameraOverlayWithCutout(
    hasCameraPermission: Boolean,
    scanRect: Rect?,
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
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val cornerRadiusPx = cutoutShape.topStart
                .toPx(Size(cutoutSize.toPx(), cutoutSize.toPx()), density)

            drawRect(color = overlayColor)
            val rect = scanRect ?: return@Canvas

            val cutoutPath = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = rect,
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
                val left = rect.left
                val top = rect.top
                val right = rect.right
                val bottom = rect.bottom

                // TOP LEFT
                drawArc(
                    color = primary,
                    startAngle = 180f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(left, top),
                    size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
                drawLine(
                    primary,
                    Offset(left + cornerRadiusPx, top),
                    Offset(left + cornerLength, top),
                    strokeWidthPx
                )
                drawLine(
                    primary,
                    Offset(left, top + cornerRadiusPx),
                    Offset(left, top + cornerLength),
                    strokeWidthPx
                )

                // TOP RIGHT
                drawArc(
                    color = primary,
                    startAngle = 270f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(right - cornerRadiusPx * 2, top),
                    size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
                drawLine(
                    primary,
                    Offset(right - cornerRadiusPx, top),
                    Offset(right - cornerLength, top),
                    strokeWidthPx
                )
                drawLine(
                    primary,
                    Offset(right, top + cornerRadiusPx),
                    Offset(right, top + cornerLength),
                    strokeWidthPx
                )

                // BOTTOM LEFT
                drawArc(
                    color = primary,
                    startAngle = 90f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(left, bottom - cornerRadiusPx * 2),
                    size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
                drawLine(
                    primary,
                    Offset(left, bottom - cornerRadiusPx),
                    Offset(left, bottom - cornerLength),
                    strokeWidthPx
                )
                drawLine(
                    primary,
                    Offset(left + cornerRadiusPx, bottom),
                    Offset(left + cornerLength, bottom),
                    strokeWidthPx
                )

                // BOTTOM RIGHT
                drawArc(
                    color = primary,
                    startAngle = 0f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(right - cornerRadiusPx * 2, bottom - cornerRadiusPx * 2),
                    size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                    style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                )
                drawLine(
                    primary,
                    Offset(right, bottom - cornerRadiusPx),
                    Offset(right, bottom - cornerLength),
                    strokeWidthPx
                )
                drawLine(
                    primary,
                    Offset(right - cornerRadiusPx, bottom),
                    Offset(right - cornerLength, bottom),
                    strokeWidthPx
                )
            }
        }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val density = LocalDensity.current
            val parentHeightPx = this.constraints.maxHeight.toFloat()

            val centerYFraction = when (deviceConfiguration) {
                DeviceConfiguration.MOBILE_LANDSCAPE -> 0.4f
                else -> 0.5f
            }

            val textOffset = when (deviceConfiguration) {
                DeviceConfiguration.MOBILE_LANDSCAPE, DeviceConfiguration.MOBILE_PORTRAIT -> 48.dp
                else -> 64.dp
            }

            val cutoutPx = with(density) { cutoutSize.toPx() }
            val targetCenterY = parentHeightPx * centerYFraction
            val yOffsetPx = (targetCenterY - (cutoutPx / 2f))
                .toInt()
                .coerceAtLeast(0)

            Text(
                text = stringResource(R.string.point_camera_qr),
                color = Color.White,
                style = when (deviceConfiguration) {
                    DeviceConfiguration.MOBILE_PORTRAIT, DeviceConfiguration.MOBILE_LANDSCAPE ->
                        MaterialTheme.typography.titleSmall

                    else -> MaterialTheme.typography.titleMedium
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset { IntOffset(x = 0, y = yOffsetPx - textOffset.toPx().toInt()) }
                    .padding(top = 16.dp)
            )

            Box(
                modifier = Modifier
                    .size(cutoutSize)
                    .align(Alignment.TopCenter)
                    .offset { IntOffset(x = 0, y = yOffsetPx) }
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
