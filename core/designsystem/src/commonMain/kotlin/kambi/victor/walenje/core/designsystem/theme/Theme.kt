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

    /** The spacing scale in DESIGN.md > Grid and spacing. */
    val spacing: WalenjeSpacing
        @Composable @ReadOnlyComposable get() = LocalWalenjeSpacing.current

    /** The radii in DESIGN.md > Shape and lines. */
    val shapes: WalenjeShapes
        @Composable @ReadOnlyComposable get() = LocalWalenjeShapes.current

    /** The line widths in DESIGN.md > Shape and lines. */
    val lines: WalenjeLines
        @Composable @ReadOnlyComposable get() = LocalWalenjeLines.current
}

/** Applies the Walenje tokens. The theme follows the system's light or dark setting. */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WalenjeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkWalenjeColors else LightWalenjeColors
    val typography = interTightTypography()
    val shapes = WalenjeShapes()
    CompositionLocalProvider(
        LocalWalenjeColors provides colors,
        LocalWalenjeTypography provides typography,
        LocalWalenjeSpacing provides WalenjeSpacing(),
        LocalWalenjeShapes provides shapes,
        LocalWalenjeLines provides WalenjeLines(),
    ) {
        MaterialExpressiveTheme(
            colorScheme = colors.toMaterialColorScheme(darkTheme),
            motionScheme = MotionScheme.expressive(),
            shapes = shapes.toMaterialShapes(),
            typography = typography.toMaterialTypography(),
            content = content,
        )
    }
}
