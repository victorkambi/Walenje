package kambi.victor.walenje.feature.welcome.view_models

import kambi.victor.walenje.core.utils.mvi.ResourceUiState
import kambi.victor.walenje.feature.welcome.SetPinContract.Effect
import kambi.victor.walenje.feature.welcome.SetPinContract.Event
import kambi.victor.walenje.feature.welcome.SetPinContract.State
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class SetPinScreenViewModelTest {
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
  fun startsIdle() {
    val viewModel = SetPinScreenViewModel()

    assertEquals(
      State(
        pin = ResourceUiState.Idle,
        confirmPin = ResourceUiState.Idle,
        validPin = ResourceUiState.Idle,
      ),
      viewModel.state.value,
    )
  }

  @Test
  fun settingThePinStoresItAndReportsIt() =
    runTest(dispatcher) {
      val viewModel = SetPinScreenViewModel()

      viewModel.setEvent(Event.SetPin("1234"))

      assertEquals(ResourceUiState.Success("1234"), viewModel.state.value.pin)
      assertEquals(listOf<Effect>(Effect.PinConfigured), viewModel.effect.take(1).toList())
    }

  @Test
  fun matchingConfirmationValidatesThePin() =
    runTest(dispatcher) {
      val viewModel = SetPinScreenViewModel()

      viewModel.setEvent(Event.SetPin("1234"))
      viewModel.setEvent(Event.SetConfirmPin("1234"))

      assertEquals(ResourceUiState.Success(true), viewModel.state.value.validPin)
      assertEquals(
        listOf(Effect.PinConfigured, Effect.ConfirmPinConfigured, Effect.PinMatched),
        viewModel.effect.take(3).toList(),
      )
    }

  @Test
  fun mismatchedConfirmationResetsBothPins() =
    runTest(dispatcher) {
      val viewModel = SetPinScreenViewModel()

      viewModel.setEvent(Event.SetPin("1234"))
      viewModel.setEvent(Event.SetConfirmPin("4321"))

      assertEquals(
        State(
          pin = ResourceUiState.Idle,
          confirmPin = ResourceUiState.Idle,
          validPin = ResourceUiState.Error(),
        ),
        viewModel.state.value,
      )
      assertEquals(
        listOf(Effect.PinConfigured, Effect.ConfirmPinConfigured, Effect.PinMismatched),
        viewModel.effect.take(3).toList(),
      )
    }

  @Test
  fun navigationEventsBecomeNavigationEffects() =
    runTest(dispatcher) {
      val viewModel = SetPinScreenViewModel()

      viewModel.setEvent(Event.NavigateBack)
      viewModel.setEvent(Event.NavigateNext)

      assertEquals(
        listOf(Effect.NavigateToBackScreen, Effect.NavigateToNextScreen),
        viewModel.effect.take(2).toList(),
      )
    }
}
