import org.gradle.accessors.dm.LibrariesForLibs

// A shared Compose Multiplatform library: Android, desktop (JVM) and, with -Pwalenje.apple=true, iOS and macOS.
plugins {
  id("com.android.kotlin.multiplatform.library")
  id("walenje-cmp")
}

val libs = the<LibrariesForLibs>()

kotlin {
  android {
    // Unique per module, as AGP 9 requires: :feature:home -> kambi.victor.walenje.feature.home
    namespace = "kambi.victor.walenje" + path.replace(':', '.')
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    minSdk = libs.versions.android.minSdk.get().toInt()
    androidResources { enable = true }
    withHostTest {}
  }

  jvm()

  if (appleTargetsEnabled) {
    iosArm64()
    iosSimulatorArm64()
    macosArm64()
  }
}
