package kambi.victor.walenje.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * The colour tokens in DESIGN.md > Colour. Read them through [WalenjeTheme.colors].
 *
 * Aqua ([accent]) is a fill for the one primary action on a screen. Put [onAccent] on it. Never use
 * aqua for text or icons on a light background.
 */
@Immutable
data class WalenjeColors(
    /** Screen background. */
    val app: Color,
    /** Tiles, fields and sheets. */
    val tile: Color,
    /** Row lines, inactive borders and bar tracks. */
    val rule: Color,
    /** Second heading line, labels and captions. */
    val muted: Color,
    /** Text, icons, bars and selected chips. */
    val ink: Color,
    /** Walenje aqua: the fill of the primary action. */
    val accent: Color,
    /** Text and icons on [accent]. */
    val onAccent: Color,
    /** The "needs review" square and badge. */
    val warn: Color,
    /** The "Review" and "Check" words. */
    val warnText: Color,
    /** Income amounts, always with a `+` sign. */
    val income: Color,
    /** Errors such as a PIN mismatch, always with a message. */
    val error: Color,
    /** Text and icons on [error]. */
    val onError: Color,
)

private val Aqua = Color(0xFF1CFDD6)
private val NearBlack = Color(0xFF050505)

val LightWalenjeColors =
    WalenjeColors(
        app = Color(0xFFF3F3F3),
        tile = Color(0xFFFFFFFF),
        rule = Color(0xFFD4D4D2),
        muted = Color(0xFF5C5C5C),
        ink = Color(0xFF0A0A0A),
        accent = Aqua,
        onAccent = NearBlack,
        warn = Color(0xFFE8A300),
        warnText = Color(0xFF875800),
        income = Color(0xFF0E7A3E),
        error = Color(0xFFC4271B),
        onError = Color(0xFFFFFFFF),
    )

val DarkWalenjeColors =
    WalenjeColors(
        app = NearBlack,
        tile = Color(0xFF161616),
        rule = Color(0xFF2B2B2B),
        muted = Color(0xFFA6A6A6),
        ink = Color(0xFFF3F3F3),
        accent = Aqua,
        onAccent = NearBlack,
        warn = Color(0xFFF2B230),
        warnText = Color(0xFFF5BD4F),
        income = Color(0xFF4ADE80),
        error = Color(0xFFFF6B5E),
        onError = NearBlack,
    )

internal val LocalWalenjeColors = staticCompositionLocalOf { LightWalenjeColors }

/**
 * Maps the tokens onto the Material roles, so that Material components follow DESIGN.md: aqua for
 * the primary role, ink and neutrals for the rest, no tonal tint on raised surfaces.
 */
fun WalenjeColors.toMaterialColorScheme(dark: Boolean): ColorScheme {
    val base = if (dark) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = accent,
        onPrimary = onAccent,
        primaryContainer = accent,
        onPrimaryContainer = onAccent,
        inversePrimary = accent,
        secondary = ink,
        onSecondary = app,
        secondaryContainer = tile,
        onSecondaryContainer = ink,
        tertiary = ink,
        onTertiary = app,
        tertiaryContainer = tile,
        onTertiaryContainer = ink,
        background = app,
        onBackground = ink,
        surface = app,
        onSurface = ink,
        surfaceVariant = tile,
        onSurfaceVariant = muted,
        surfaceTint = Color.Transparent,
        inverseSurface = ink,
        inverseOnSurface = app,
        error = error,
        onError = onError,
        errorContainer = tile,
        onErrorContainer = error,
        outline = ink,
        outlineVariant = rule,
        scrim = Color.Black,
        surfaceBright = tile,
        surfaceContainer = tile,
        surfaceContainerHigh = tile,
        surfaceContainerHighest = tile,
        surfaceContainerLow = tile,
        surfaceContainerLowest = tile,
        surfaceDim = app,
    )
}
