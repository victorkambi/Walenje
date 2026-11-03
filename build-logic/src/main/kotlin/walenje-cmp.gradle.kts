import org.gradle.accessors.dm.LibrariesForLibs

plugins {
  id("walenje-kmp")
  id("org.jetbrains.kotlin.plugin.compose")
  id("org.jetbrains.compose")
}

val libs = the<LibrariesForLibs>()

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.compose.foundation)
      implementation(libs.compose.material3)
      implementation(libs.compose.runtime)
      implementation(libs.compose.ui)
      implementation(libs.jetbrains.lifecycle.runtime)
      implementation(libs.jetbrains.lifecycle.runtime.compose)
      implementation(libs.jetbrains.lifecycle.viewmodel)
    }
  }
}
