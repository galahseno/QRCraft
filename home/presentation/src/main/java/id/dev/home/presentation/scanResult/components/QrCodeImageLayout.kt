package id.dev.home.presentation.scanResult.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import id.dev.core.presentation.R

@Composable
fun BoxScope.QrCodeImageLayout() {
    Surface(
        modifier = Modifier
            .size(160.dp)
            .align(Alignment.TopCenter)
            .offset(y = (-80).dp),
        shape = RoundedCornerShape(36.dp),
        color = Color.White,
        shadowElevation = 4.dp
    ) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.scan),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentScale = ContentScale.Fit
        )
    }
}