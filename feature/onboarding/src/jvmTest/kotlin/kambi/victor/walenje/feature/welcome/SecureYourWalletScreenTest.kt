package kambi.victor.walenje.feature.welcome

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.test.v2.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class SecureYourWalletScreenTest {
    @Test
    fun showsTheHeadlineAndExplanation() = runComposeUiTest {
        setContent { SecureYourWalletScreen(onNavigateToNext = {}) }

        onNodeWithText("SECURE\nYOUR\nWALLET", substring = true).assertIsDisplayed()
        onNodeWithText("We need to be sure that it’s you", substring = true).assertIsDisplayed()
    }

    @Test
    fun theProceedButtonNavigatesOnce() = runComposeUiTest {
        var navigations = 0
        setContent { SecureYourWalletScreen(onNavigateToNext = { navigations++ }) }

        onNodeWithText("Proceed to secure Wallet").performClick()

        assertEquals(1, navigations)
    }

    @Test
    fun aLongSwipeUpNavigatesOnce() = runComposeUiTest {
        var navigations = 0
        setContent { SecureYourWalletScreen(onNavigateToNext = { navigations++ }) }

        // Starts above the button, so a tap on it can't count as the swipe.
        onRoot().performTouchInput { swipeUp(startY = centerY, endY = top) }
        waitForIdle()

        assertEquals(1, navigations)
    }

    @Test
    fun aShortSwipeUpStaysOnTheScreen() = runComposeUiTest {
        var navigations = 0
        setContent { SecureYourWalletScreen(onNavigateToNext = { navigations++ }) }

        // Under the 30% drag threshold, and slow enough not to count as a flick.
        onRoot().performTouchInput {
            swipe(start = center, end = center - Offset(0f, height * 0.1f), durationMillis = 1_000)
        }
        waitForIdle()

        assertEquals(0, navigations)
        onNodeWithText("Proceed to secure Wallet").assertIsDisplayed()
    }
}
