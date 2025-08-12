package id.dev.home.presentation.scanResult.results

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.dev.core.presentation.R
import id.dev.home.presentation.model.BarcodeResult
import id.dev.home.presentation.scanResult.ActionButtonsLayout
import kotlin.jvm.java

@Composable
internal fun TextResultContent(
    modifier: Modifier = Modifier,
    linkResult: BarcodeResult.Text
) {
    Box(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
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
                    text = linkResult::class.java.simpleName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )
                Text(
                    text = linkResult.content,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .align(alignment = Alignment.Start)
                )

                Spacer(modifier = Modifier.height(24.dp))

                ActionButtonsLayout(
                    share = linkResult.content,
                    copyToClipboard = linkResult.content
                )
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