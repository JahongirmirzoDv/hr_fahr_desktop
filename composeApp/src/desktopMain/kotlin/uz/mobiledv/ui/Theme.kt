package uz.mobiledv.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Purple,
    onPrimary = Color.White,
    primaryContainer = DeepPurple,
    onPrimaryContainer = Color.White,
    secondary = Teal,
    onSecondary = Color.Black,
    secondaryContainer = DeepTeal,
    onSecondaryContainer = Color.White,
    tertiary = LightPurple,
    onTertiary = Color.Black,
    error = Error,
    onError = Color.White,
    errorContainer = Error.copy(alpha = 0.2f),
    onErrorContainer = Color.White,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = SurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = CardDarkBg,
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = Color(0xFF313033),
    inversePrimary = Color(0xFF6750A4),
    surfaceTint = Purple
)

private val LightColorScheme = lightColorScheme(
    primary = Purple,
    onPrimary = Color.White,
    primaryContainer = DeepPurple,
    onPrimaryContainer = Color.White,
    secondary = Teal,
    onSecondary = Color.Black,
    secondaryContainer = DeepTeal,
    onSecondaryContainer = Color.White,
    tertiary = LightPurple,
    onTertiary = Color.Black,
    error = Error,
    onError = Color.White,
    errorContainer = Error.copy(alpha = 0.1f),
    onErrorContainer = Error,
    background = LightBackground,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = CardLightBg,
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFFD0BCFF),
    surfaceTint = Purple
)

// Custom local provider for status colors
data class StatusColors(
    val success: Color,
    val error: Color,
    val warning: Color,
    val info: Color,
    val successContainer: Color,
    val errorContainer: Color,
    val warningContainer: Color,
    val infoContainer: Color
)

val LocalStatusColors = staticCompositionLocalOf {
    StatusColors(
        success = Success,
        error = Error,
        warning = Warning,
        info = Info,
        successContainer = StatusSuccessLight,
        errorContainer = StatusErrorLight,
        warningContainer = StatusWarningLight,
        infoContainer = StatusInfoLight
    )
}

@Composable
fun HRDesktopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val statusColors = StatusColors(
        success = Success,
        error = Error,
        warning = Warning,
        info = Info,
        successContainer = StatusSuccessLight,
        errorContainer = StatusErrorLight,
        warningContainer = StatusWarningLight,
        infoContainer = StatusInfoLight
    )

    CompositionLocalProvider(
        LocalStatusColors provides statusColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography(),
            content = content
        )
    }
}