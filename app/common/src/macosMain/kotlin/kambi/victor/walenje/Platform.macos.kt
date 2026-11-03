package kambi.victor.walenje

actual fun getPlatform(): Platform = MacOSPlatform()

class MacOSPlatform : Platform {
  override val name: String = "macOS"
}
