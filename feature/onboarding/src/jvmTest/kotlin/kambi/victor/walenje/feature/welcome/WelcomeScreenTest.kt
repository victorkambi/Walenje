package kambi.victor.walenje.feature.welcome

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class WelcomeScreenTest {
    @Test
    fun showsTheHeadlineAndTagline() = runComposeUiTest {
        setContent { Welcome(onNavigateToSecureWallet = {}) }

        onNodeWithText("CREATE\nYOUR\nPRIVATE\nWALLET", substring = true).assertIsDisplayed()
        onNodeWithText("Secure your money", substring = true).assertIsDisplayed()
    }

    @Test
    fun theContinueButtonIsAnAccessibleButton() = runComposeUiTest {
        setContent { Welcome(onNavigateToSecureWallet = {}) }

        onNodeWithContentDescription("Continue")
            .assertIsDisplayed()
            .assertHasClickAction()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Button))
    }

    @Test
    fun continuingNavigatesToTheSecureWalletScreenOnce() = runComposeUiTest {
        var navigations = 0
        setContent { Welcome(onNavigateToSecureWallet = { navigations++ }) }

        onNodeWithContentDescription("Continue").performClick()

        assertEquals(1, navigations)
    }
}
