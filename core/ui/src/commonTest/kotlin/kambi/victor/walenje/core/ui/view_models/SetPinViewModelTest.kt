package kambi.victor.walenje.core.ui.view_models

import kotlin.test.Test
import kotlin.test.assertEquals

class SetPinViewModelTest {
  @Test
  fun startsEmpty() {
    assertEquals("", SetPinViewModel().pin.value)
  }

  @Test
  fun acceptsUpToSixDigits() {
    val viewModel = SetPinViewModel()

    viewModel.onPinChange("123456")

    assertEquals("123456", viewModel.pin.value)
  }

  @Test
  fun ignoresEntriesLongerThanSixDigits() {
    val viewModel = SetPinViewModel()
    viewModel.onPinChange("123456")

    viewModel.onPinChange("1234567")

    assertEquals("123456", viewModel.pin.value)
  }

  @Test
  fun clearsThePin() {
    val viewModel = SetPinViewModel()
    viewModel.onPinChange("12")

    viewModel.onPinChange("")

    assertEquals("", viewModel.pin.value)
  }
}
