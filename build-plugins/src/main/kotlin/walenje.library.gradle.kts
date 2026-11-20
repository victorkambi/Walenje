import org.gradle.accessors.dm.LibrariesForLibs

// A shared Compose Multiplatform library: Android, iOS (with -Pwalenje.ios=true) and JVM. The JVM
// target exists only for the desktop dev app (Compose Hot Reload) and fast tests; it isn't shipped.
plugins {
  id("com.android.kotlin.multiplatform.library")
  id("walenje.compose")
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

  if (iosTargetsEnabled) {
    iosArm64()
    iosSimulatorArm64()
  }

  sourceSets {
    // Compose UI tests run on the JVM only: in commonTest they would also run as Android host
    // tests, which need Robolectric.
    jvmTest.dependencies {
      implementation(libs.compose.ui.test)
      implementation(compose.desktop.currentOs)
    }
  }
}
