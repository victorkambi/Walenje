package kambi.victor.walenje.core.utils.mvi

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
class BaseViewModelTest {
  private sealed interface Event : UiEvent {
    data class Add(val amount: Int) : Event

    data object Report : Event
  }

  private data class State(val count: Int, val label: String) : UiState

  private data class Effect(val count: Int) : UiEffect

  private class CounterViewModel : BaseViewModel<Event, State, Effect>() {
    val handled = mutableListOf<Event>()

    override fun createInitialState() = State(count = 0, label = "counter")

    override fun handleEvent(event: Event) {
      handled += event
      when (event) {
        is Event.Add -> setState { copy(count = count + event.amount) }
        Event.Report -> setEffect { Effect(state.value.count) }
      }
    }
  }

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
  fun stateStartsFromTheInitialState() {
    assertEquals(State(count = 0, label = "counter"), CounterViewModel().state.value)
  }

  @Test
  fun eventsReachTheHandlerInOrder() =
    runTest(dispatcher) {
      val viewModel = CounterViewModel()

      viewModel.setEvent(Event.Add(1))
      viewModel.setEvent(Event.Report)
      viewModel.setEvent(Event.Add(2))

      assertEquals(listOf(Event.Add(1), Event.Report, Event.Add(2)), viewModel.handled)
    }

  @Test
  fun setStateReducesTheCurrentStateAndKeepsOtherFields() =
    runTest(dispatcher) {
      val viewModel = CounterViewModel()

      viewModel.setEvent(Event.Add(2))
      viewModel.setEvent(Event.Add(3))

      assertEquals(State(count = 5, label = "counter"), viewModel.state.value)
    }

  @Test
  fun effectsAreDeliveredOnceAndInOrder() =
    runTest(dispatcher) {
      val viewModel = CounterViewModel()

      viewModel.setEvent(Event.Report)
      viewModel.setEvent(Event.Add(4))
      viewModel.setEvent(Event.Report)

      assertEquals(listOf(Effect(0), Effect(4)), viewModel.effect.take(2).toList())
    }
}
