package kambi.victor.walenje.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import walenje.core.designsystem.generated.resources.InterTight
import walenje.core.designsystem.generated.resources.Res

/** Turns on tabular figures, so that digits line up in columns. */
internal const val TABULAR_FIGURES = "tnum"

/**
 * The seven text styles in DESIGN.md > Typography. Every line height is on the 4dp baseline.
 *
 * Read them through [WalenjeTheme.typography]. The label style is set in capitals: `WalenjeText`
 * does that for you.
 */
@Immutable
data class WalenjeTypography(
    /** The hero number. One per screen. */
    val display: TextStyle,
    /** Screen heading, two-tone. */
    val headline: TextStyle,
    /** Detail screen titles and sheet titles. */
    val title: TextStyle,
    /** Running text and field values. */
    val body: TextStyle,
    /** Amounts in lists and fields. */
    val amount: TextStyle,
    /** Section labels, column heads and units such as "KES". */
    val label: TextStyle,
    /** Secondary lines, such as the date and category under a merchant. */
    val caption: TextStyle,
)

/** Builds the Walenje text styles in [fontFamily]. */
fun walenjeTypography(fontFamily: FontFamily = FontFamily.Default): WalenjeTypography {
    fun style(
        size: Int,
        lineHeight: Int,
        weight: FontWeight,
        tracking: Float = 0f,
        features: String? = null,
    ) =
        TextStyle(
            fontFamily = fontFamily,
            fontSize = size.sp,
            lineHeight = lineHeight.sp,
            fontWeight = weight,
            letterSpacing = tracking.em,
            fontFeatureSettings = features,
        )

    return WalenjeTypography(
        display = style(64, 60, FontWeight.SemiBold, tracking = -0.04f, features = TABULAR_FIGURES),
        headline = style(28, 32, FontWeight.SemiBold, tracking = -0.02f),
        title = style(20, 24, FontWeight.SemiBold, tracking = -0.01f),
        body = style(16, 24, FontWeight.Normal),
        amount = style(16, 24, FontWeight.Medium, features = TABULAR_FIGURES),
        label = style(12, 16, FontWeight.SemiBold, tracking = 0.04f),
        caption = style(14, 20, FontWeight.Normal),
    )
}

/**
 * Maps the Walenje styles onto the Material roles, so that Material components use the same scale.
 * Button text (`labelLarge`) is body in semibold.
 */
fun WalenjeTypography.toMaterialTypography(): Typography =
    Typography(
        displayLarge = display,
        displayMedium = display,
        displaySmall = display,
        headlineLarge = headline,
        headlineMedium = headline,
        headlineSmall = headline,
        titleLarge = title,
        titleMedium = title,
        titleSmall = title,
        bodyLarge = body,
        bodyMedium = body,
        bodySmall = caption,
        labelLarge = body.copy(fontWeight = FontWeight.SemiBold),
        labelMedium = label,
        labelSmall = label,
    )

internal val LocalWalenjeTypography = staticCompositionLocalOf { walenjeTypography() }

/** The Walenje text styles in Inter Tight. */
@Composable fun interTightTypography(): WalenjeTypography = walenjeTypography(interTightFamily())

/** Inter Tight is a variable font: each weight sets the font's weight axis. */
@Composable
fun interTightFamily(): FontFamily =
    FontFamily(
        Font(Res.font.InterTight, FontWeight.Normal),
        Font(Res.font.InterTight, FontWeight.Medium),
        Font(Res.font.InterTight, FontWeight.SemiBold),
        Font(Res.font.InterTight, FontWeight.Bold),
    )
