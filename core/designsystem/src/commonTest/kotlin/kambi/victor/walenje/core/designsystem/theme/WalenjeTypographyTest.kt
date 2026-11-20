package kambi.victor.walenje.core.designsystem.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WalenjeTypographyTest {
    private val typography = walenjeTypography()

    private val styles: Map<String, TextStyle> =
        mapOf(
            "display" to typography.display,
            "headline" to typography.headline,
            "title" to typography.title,
            "body" to typography.body,
            "amount" to typography.amount,
            "label" to typography.label,
            "caption" to typography.caption,
        )

    @Test
    fun everyLineHeightIsOnTheFourDpBaseline() {
        styles.forEach { (name, style) ->
            assertEquals(0f, style.lineHeight.value % 4, "$name line height ${style.lineHeight}")
        }
    }

    @Test
    fun numbersUseTabularFigures() {
        assertEquals(TABULAR_FIGURES, typography.display.fontFeatureSettings)
        assertEquals(TABULAR_FIGURES, typography.amount.fontFeatureSettings)
    }

    @Test
    fun displayIsTheLargestStyle() {
        val others = styles - "display"
        others.forEach { (name, style) ->
            assertTrue(typography.display.fontSize.value > style.fontSize.value, name)
        }
    }

    @Test
    fun largeStylesHaveTightTracking() {
        listOf(typography.display, typography.headline, typography.title).forEach {
            assertTrue(it.letterSpacing.value < 0f, "$it")
        }
    }

    @Test
    fun weightsStayInTheScale() {
        val allowed = setOf(FontWeight.Normal, FontWeight.Medium, FontWeight.SemiBold)
        styles.forEach { (name, style) -> assertTrue(style.fontWeight in allowed, name) }
    }

    @Test
    fun materialButtonTextIsBodyInSemibold() {
        val material = typography.toMaterialTypography()
        assertEquals(typography.body.fontSize, material.labelLarge.fontSize)
        assertEquals(FontWeight.SemiBold, material.labelLarge.fontWeight)
        assertEquals(typography.display, material.displayLarge)
    }
}
