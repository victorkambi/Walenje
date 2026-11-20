package kambi.victor.walenje.core.designsystem.theme

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WalenjeShapeAndSpacingTest {
    private val spacing = WalenjeSpacing()
    private val shapes = WalenjeShapes()
    private val density = Density(1f)
    private val tile = Size(100f, 100f)

    @Test
    fun spacingStaysOnTheFourDpBaseline() {
        spacing.scale.forEach { assertEquals(0f, it.value % 4, "$it") }
        assertEquals(spacing.scale.sorted(), spacing.scale)
    }

    @Test
    fun rolesMatchDesignMd() {
        assertEquals(16.dp, spacing.margin)
        assertEquals(8.dp, spacing.gutter)
        assertEquals(12.dp, spacing.tilePadding)
        assertEquals(16.dp, spacing.blockGap)
        assertEquals(48.dp, spacing.touchTarget)
        assertEquals(4, GRID_COLUMNS)
    }

    @Test
    fun radiiAreFourAndEightAndFull() {
        assertEquals(4f, shapes.small.topStart.toPx(tile, density))
        assertEquals(8f, shapes.medium.topStart.toPx(tile, density))
        assertEquals(50f, shapes.full.topStart.toPx(tile, density))
    }

    @Test
    fun materialShapesStaySmall() {
        val material = shapes.toMaterialShapes()
        listOf(
                material.extraSmall,
                material.small,
                material.medium,
                material.large,
                material.extraLarge,
            )
            .forEach { assertTrue(it.topStart.toPx(tile, density) <= 8f) }
    }

    @Test
    fun linesAreHairlineAndStrong() {
        val lines = WalenjeLines()
        assertEquals(1.dp, lines.hairline)
        assertEquals(1.5.dp, lines.strong)
    }
}
