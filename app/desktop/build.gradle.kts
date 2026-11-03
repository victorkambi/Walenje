import org.jetbrains.compose.desktop.application.dsl.TargetFormat

// Desktop (JVM) app. Run it with ./gradlew :app:desktop:run, or :app:desktop:hotRunJvm for
// Compose Hot Reload.
plugins {
  id("walenje-cmp")
  id("org.jetbrains.compose.hot-reload")
}

kotlin {
  jvm()

  sourceSets {
    jvmMain.dependencies {
      implementation(projects.app.common)
      implementation(projects.core.authentication)
      implementation(compose.desktop.currentOs)
      implementation(libs.kotlinx.coroutines.swing)
    }
  }
}

compose.desktop {
  application {
    mainClass = "kambi.victor.walenje.MainKt"
    nativeDistributions {
      packageName = "Walenje"
      packageVersion = "1.0.0"
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
    }
  }
}
