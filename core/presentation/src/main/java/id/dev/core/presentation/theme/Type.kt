package id.dev.core.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import id.dev.core.presentation.R

val suse = FontFamily(
    Font(R.font.suse_regular, FontWeight.Normal),
    Font(R.font.suse_medium, FontWeight.Medium),
    Font(R.font.suse_semi_bold, FontWeight.SemiBold),
)

val Typography = Typography(
    titleMedium = TextStyle(
        fontFamily = suse,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 32.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = suse,
        fontWeight = FontWeight.SemiBold,
        fontSize = 19.sp,
        lineHeight = 24.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = suse,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = suse,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
)