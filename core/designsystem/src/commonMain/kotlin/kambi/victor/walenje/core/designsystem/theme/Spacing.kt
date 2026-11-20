package kambi.victor.walenje.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The spacing scale in DESIGN.md > Grid and spacing. Read it through [WalenjeTheme.spacing].
 *
 * Each step is named by its count of 4dp baseline units: [x4] is 4 × 4dp = 16dp. Prefer the named
 * roles ([margin], [gutter], ...) where one fits.
 */
@Immutable
data class WalenjeSpacing(
    val x1: Dp = 4.dp,
    val x2: Dp = 8.dp,
    val x3: Dp = 12.dp,
    val x4: Dp = 16.dp,
    val x6: Dp = 24.dp,
    val x8: Dp = 32.dp,
    val x12: Dp = 48.dp,
) {
    /** Side margin of a screen. */
    val margin: Dp
        get() = x4

    /** Gap between grid columns and between tiles. */
    val gutter: Dp
        get() = x2

    /** Padding inside a tile. */
    val tilePadding: Dp
        get() = x3

    /** Gap between blocks on a screen. */
    val blockGap: Dp
        get() = x4

    /** Smallest touch target. */
    val touchTarget: Dp
        get() = x12

    /** The scale from smallest to largest. */
    val scale: List<Dp>
        get() = listOf(x1, x2, x3, x4, x6, x8, x12)
}

/** Columns of the screen grid in DESIGN.md > Grid and spacing. */
const val GRID_COLUMNS = 4

internal val LocalWalenjeSpacing = staticCompositionLocalOf { WalenjeSpacing() }
