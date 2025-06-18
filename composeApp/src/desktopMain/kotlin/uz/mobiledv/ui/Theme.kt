package uz.mobiledv.ui


import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AccentColor,
    secondary = LightBlue,
    background = DarkBlue,
    surface = LightBlue,
    onPrimary = TextColor,
    onSecondary = TextColor,
    onBackground = TextColor,
    onSurface = TextColor,
)

@Composable
fun HRDesktopTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = MaterialTheme.typography,
            content = content
    )
}