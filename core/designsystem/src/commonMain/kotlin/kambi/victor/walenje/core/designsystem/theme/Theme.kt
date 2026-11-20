package kambi.victor.walenje.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

/** Reads the Walenje design tokens inside [WalenjeTheme]. */
object WalenjeTheme {
    /** The colour tokens in DESIGN.md > Colour. */
    val colors: WalenjeColors
        @Composable @ReadOnlyComposable get() = LocalWalenjeColors.current

    /** The text styles in DESIGN.md > Typography. */
    val typography: WalenjeTypography
        @Composable @ReadOnlyComposable get() = LocalWalenjeTypography.current
}

/** Applies the Walenje tokens. The theme follows the system's light or dark setting. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WalenjeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkWalenjeColors else LightWalenjeColors
    val typography = interTightTypography()
    CompositionLocalProvider(
        LocalWalenjeColors provides colors,
        LocalWalenjeTypography provides typography,
    ) {
        MaterialExpressiveTheme(
            colorScheme = colors.toMaterialColorScheme(darkTheme),
            motionScheme = MotionScheme.expressive(),
            typography = typography.toMaterialTypography(),
            content = content,
        )
    }
}
