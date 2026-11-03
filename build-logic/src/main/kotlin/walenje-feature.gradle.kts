import org.gradle.accessors.dm.LibrariesForLibs

// A feature module: screens, ViewModels and a Koin module, on top of the core modules.
plugins {
  id("walenje-kmp-library")
  id("walenje-kotlinx")
  id("walenje-koin")
  id("walenje-logger")
}

val libs = the<LibrariesForLibs>()

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(libs.compose.navigation)
      api(project(":core:ui"))
      api(project(":core:utils"))
      api(project(":core:authentication"))
    }
  }
}
