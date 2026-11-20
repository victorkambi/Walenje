package kambi.victor.walenje.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import walenje.core.designsystem.generated.resources.InterTight
import walenje.core.designsystem.generated.resources.Res

@Composable
fun interTightTypography(): Typography {
    val interTightFont = interTightFamily()

    return with(MaterialTheme.typography) {
        copy(
            displayLarge = displayLarge.copy(fontFamily = interTightFont),
            displayMedium = displayMedium.copy(fontFamily = interTightFont),
            displaySmall = displaySmall.copy(fontFamily = interTightFont),
            headlineLarge = headlineLarge.copy(fontFamily = interTightFont),
            headlineMedium = headlineMedium.copy(fontFamily = interTightFont),
            headlineSmall = headlineSmall.copy(fontFamily = interTightFont),
            titleLarge = titleLarge.copy(fontFamily = interTightFont),
            titleMedium = titleMedium.copy(fontFamily = interTightFont),
            titleSmall = titleSmall.copy(fontFamily = interTightFont),
            bodyLarge = bodyLarge.copy(fontFamily = interTightFont),
            bodyMedium = bodyMedium.copy(fontFamily = interTightFont),
            bodySmall = bodySmall.copy(fontFamily = interTightFont),
            labelLarge = labelLarge.copy(fontFamily = interTightFont),
            labelMedium = labelMedium.copy(fontFamily = interTightFont),
            labelSmall = labelSmall.copy(fontFamily = interTightFont),
        )
    }
}

@Composable
fun interTightFamily(): FontFamily =
    FontFamily(
        Font(Res.font.InterTight, FontWeight.Light),
        Font(Res.font.InterTight, FontWeight.Normal),
        Font(Res.font.InterTight, FontWeight.Medium),
        Font(Res.font.InterTight, FontWeight.SemiBold),
        Font(Res.font.InterTight, FontWeight.Bold),
    )
