// Shared app entry: App(), navigation, Koin wiring, and the iOS framework the Xcode project embeds.
plugins {
  id("walenje-kmp-library")
  id("walenje-kotlinx")
  id("walenje-koin")
  id("walenje-logger")
}

kotlin {
  appleTargets {
    if (konanTarget.family.isAppleFamily && name.startsWith("ios")) {
      binaries.framework {
        baseName = "common"
        isStatic = true
      }
    }
  }

  sourceSets {
    commonMain.dependencies {
      implementation(projects.core.designsystem)
      implementation(projects.core.authentication)
      implementation(projects.core.ui)
      implementation(projects.feature.onboarding)
      implementation(projects.feature.home)
      implementation(projects.feature.analytics)
      implementation(projects.feature.profile)
      implementation(libs.compose.navigation)
    }
  }
}
