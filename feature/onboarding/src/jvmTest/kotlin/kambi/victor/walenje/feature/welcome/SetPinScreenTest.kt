package kambi.victor.walenje.feature.welcome

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kambi.victor.walenje.feature.welcome.SetPinContract.Event
import kambi.victor.walenje.feature.welcome.view_models.SetPinScreenViewModel
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalTestApi::class, ExperimentalCoroutinesApi::class)
class SetPinScreenTest {
  private val dispatcher = UnconfinedTestDispatcher()

  @BeforeTest
  fun setUp() {
    Dispatchers.setMain(dispatcher)
  }

  @AfterTest
  fun tearDown() {
    Dispatchers.resetMain()
  }

  @Test
  fun aFullPinAsksToReEnterIt() = runComposeUiTest {
    showScreen()

    onNodeWithText("Enter your PIN").assertIsDisplayed()
    enterPin("1234")

    waitUntilShown("Re-enter your PIN")
  }

  @Test
  fun aMatchingConfirmationNavigatesHome() = runComposeUiTest {
    var navigatedHome = false
    showScreen(onNavigateToHomeScreen = { navigatedHome = true })

    enterPin("1234")
    waitUntilShown("Re-enter your PIN")
    enterPin("1234")

    waitUntil(timeoutMillis = 5_000) { navigatedHome }
  }

  @Test
  fun aMismatchedConfirmationStartsOver() = runComposeUiTest {
    var navigatedHome = false
    showScreen(onNavigateToHomeScreen = { navigatedHome = true })

    enterPin("1234")
    waitUntilShown("Re-enter your PIN")
    enterPin("4321")

    waitUntilShown("Try entering a new Pin")
    onNodeWithText("Enter your PIN").assertIsDisplayed()
    assertFalse(navigatedHome)
  }

  // Regression: one effect keyed on both PINs resent SetPin on every confirmation digit.
  @Test
  fun eachPinIsSentToTheViewModelOnce() = runComposeUiTest {
    val viewModel = SetPinScreenViewModel()
    val events = mutableListOf<Event>()
    val collector = CoroutineScope(dispatcher)
    collector.launch { viewModel.event.collect { events += it } }
    var navigatedHome = false
    showScreen(viewModel, onNavigateToHomeScreen = { navigatedHome = true })

    enterPin("1234")
    waitUntilShown("Re-enter your PIN")
    enterPin("1234")
    waitUntil(timeoutMillis = 5_000) { navigatedHome }
    collector.cancel()

    assertEquals(listOf(Event.SetPin("1234"), Event.SetConfirmPin("1234")), events)
  }

  // Passes the ViewModel in, so the tests don't need Koin.
  private fun ComposeUiTest.showScreen(
    viewModel: SetPinScreenViewModel = SetPinScreenViewModel(),
    onNavigateToHomeScreen: () -> Unit = {},
  ) {
    setContent {
      SetPinScreen(
        onNavigateBack = {},
        viewModel = viewModel,
        onNavigateToHomeScreen = onNavigateToHomeScreen,
      )
    }
  }

  private fun ComposeUiTest.enterPin(pin: String) {
    pin.forEach { digit -> onNodeWithText(digit.toString()).performClick() }
    waitForIdle()
  }

  private fun ComposeUiTest.waitUntilShown(text: String) {
    waitUntil(timeoutMillis = 5_000) { onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }
  }
}
