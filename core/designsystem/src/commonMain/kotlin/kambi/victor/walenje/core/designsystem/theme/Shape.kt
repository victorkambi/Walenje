package kambi.victor.walenje.core.designsystem.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** The shapes in DESIGN.md > Shape and lines. Read them through [WalenjeTheme.shapes]. */
@Immutable
data class WalenjeShapes(
    /** 4dp: chips, fields, segmented controls and tags. */
    val small: CornerBasedShape = RoundedCornerShape(4.dp),
    /** 8dp: tiles, photo frames and notes. */
    val medium: CornerBasedShape = RoundedCornerShape(8.dp),
    /** Full radius: pill buttons and circle icon buttons. */
    val full: CornerBasedShape = CircleShape,
)

/** The line widths in DESIGN.md > Shape and lines. Read them through [WalenjeTheme.lines]. */
@Immutable
data class WalenjeLines(
    /** 1dp: the rule between rows. */
    val hairline: Dp = 1.dp,
    /** 1.5dp: the ink rule under a group header and the outline of outlined controls. */
    val strong: Dp = 1.5.dp,
)

/**
 * Maps the shapes onto the Material roles, so that Material components stay on 4dp and 8dp radii.
 * Material buttons keep their own full-radius shape.
 */
fun WalenjeShapes.toMaterialShapes(): Shapes =
    Shapes(
        extraSmall = small,
        small = small,
        medium = medium,
        large = medium,
        extraLarge = medium,
    )

internal val LocalWalenjeShapes = staticCompositionLocalOf { WalenjeShapes() }

internal val LocalWalenjeLines = staticCompositionLocalOf { WalenjeLines() }
