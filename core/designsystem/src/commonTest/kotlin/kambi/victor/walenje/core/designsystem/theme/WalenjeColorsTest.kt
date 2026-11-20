package kambi.victor.walenje.core.designsystem.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WalenjeColorsTest {
    private val themes = mapOf("light" to LightWalenjeColors, "dark" to DarkWalenjeColors)

    /** WCAG 2.x contrast ratio. */
    private fun contrast(a: Color, b: Color): Float {
        val (hi, lo) = listOf(a.luminance(), b.luminance()).sortedDescending()
        return (hi + 0.05f) / (lo + 0.05f)
    }

    private fun assertAa(name: String, fg: Color, bg: Color, min: Float = 4.5f) {
        val ratio = contrast(fg, bg)
        assertTrue(ratio >= min, "$name: $ratio < $min")
    }

    @Test
    fun readableTextMeetsAaOnScreensAndTiles() {
        themes.forEach { (theme, c) ->
            listOf("app" to c.app, "tile" to c.tile).forEach { (surface, bg) ->
                assertAa("$theme ink on $surface", c.ink, bg)
                assertAa("$theme muted on $surface", c.muted, bg)
                assertAa("$theme warnText on $surface", c.warnText, bg)
                assertAa("$theme income on $surface", c.income, bg)
                assertAa("$theme error on $surface", c.error, bg)
            }
        }
    }

    @Test
    fun textOnFillsMeetsAa() {
        themes.forEach { (theme, c) ->
            assertAa("$theme onAccent on accent", c.onAccent, c.accent)
            assertAa("$theme onError on error", c.onError, c.error)
        }
    }

    @Test
    fun aquaIsNeverReadableAsTextOnLight() {
        // DESIGN.md: aqua is a fill on light backgrounds, never text. Guard the reason.
        val c = LightWalenjeColors
        assertTrue(contrast(c.accent, c.app) < 3f)
    }

    @Test
    fun materialPrimaryIsAquaAndSurfacesAreNeutral() {
        themes.forEach { (theme, c) ->
            val scheme = c.toMaterialColorScheme(dark = theme == "dark")
            assertEquals(c.accent, scheme.primary, theme)
            assertEquals(c.onAccent, scheme.onPrimary, theme)
            assertEquals(c.app, scheme.background, theme)
            assertEquals(c.ink, scheme.onSurface, theme)
            assertEquals(c.muted, scheme.onSurfaceVariant, theme)
            assertEquals(Color.Transparent, scheme.surfaceTint, theme)
        }
    }
}
