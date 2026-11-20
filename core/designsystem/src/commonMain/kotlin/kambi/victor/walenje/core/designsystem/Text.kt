package kambi.victor.walenje.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.TextUnit
import kambi.victor.walenje.core.designsystem.theme.WalenjeTheme

/** The text styles in DESIGN.md > Typography. */
enum class TextType {
    Display,
    Headline,
    Title,
    Body,
    Amount,
    Label,
    Caption,
}

@Composable
@ReadOnlyComposable
fun WalenjeTextStyle(textType: TextType): TextStyle {
    val typography = WalenjeTheme.typography
    return when (textType) {
        TextType.Display -> typography.display
        TextType.Headline -> typography.headline
        TextType.Title -> typography.title
        TextType.Body -> typography.body
        TextType.Amount -> typography.amount
        TextType.Label -> typography.label
        TextType.Caption -> typography.caption
    }
}

/**
 * The muted colour for the second line of a two-tone heading and for captions.
 *
 * Uses the Material palette until the DESIGN.md colour tokens replace it.
 */
@Composable
@ReadOnlyComposable
private fun mutedColor(color: Color, muted: Boolean): Color =
    if (muted && color == Color.Unspecified) MaterialTheme.colorScheme.onSurfaceVariant else color

/**
 * Text in a Walenje style. [TextType.Label] is set in capitals. [muted] picks the muted colour when
 * no [color] is given.
 */
@Composable
fun WalenjeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    textType: TextType = TextType.Body,
    muted: Boolean = false,
) {
    Text(
        text = if (textType == TextType.Label) text.uppercase() else text,
        modifier = modifier,
        color = mutedColor(color, muted),
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        style = WalenjeTextStyle(textType),
    )
}

@Composable
fun WalenjeText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    textType: TextType = TextType.Body,
    muted: Boolean = false,
) {
    Text(
        text = if (textType == TextType.Label) text.toUpperCase() else text,
        modifier = modifier,
        color = mutedColor(color, muted),
        fontWeight = fontWeight,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        style = WalenjeTextStyle(textType),
    )
}

@Composable fun WalenjeBasicText() {}
