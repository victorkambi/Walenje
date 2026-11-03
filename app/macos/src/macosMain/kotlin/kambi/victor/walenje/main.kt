package kambi.victor.walenje

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import kambi.victor.walenje.core.authentication.BiometricPromptManager
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

private val biometrics by lazy { BiometricPromptManager() }

fun main() {
  NSApplication.sharedApplication()
  Window(title = "Walenje", size = DpSize(420.dp, 860.dp)) { App(biometrics) }
  NSApp?.run()
}
