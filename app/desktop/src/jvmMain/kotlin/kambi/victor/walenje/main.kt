package kambi.victor.walenje

import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Taskbar
import javax.imageio.ImageIO
import kambi.victor.walenje.core.authentication.BiometricPromptManager

private const val APP_NAME = "Walenje"

private val biometrics by lazy { BiometricPromptManager() }

fun main() {
  // macOS reads the app name for the menu bar and Dock when AWT starts, so set it first.
  System.setProperty("apple.awt.application.name", APP_NAME)

  val icon =
    checkNotNull(object {}.javaClass.getResourceAsStream("/walenje_icon.png")).use {
      ImageIO.read(it)
    }
  if (
    Taskbar.isTaskbarSupported() && Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_IMAGE)
  ) {
    Taskbar.getTaskbar().iconImage = icon
  }
  val windowIcon = BitmapPainter(icon.toComposeImageBitmap())

  application {
    Window(onCloseRequest = ::exitApplication, title = APP_NAME, icon = windowIcon) {
      App(biometrics)
    }
  }
}
