rootProject.name = "Walenje"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
  includeBuild("build-logic")
  repositories {
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google {
      mavenContent {
        includeGroupAndSubgroups("androidx")
        includeGroupAndSubgroups("com.android")
        includeGroupAndSubgroups("com.google")
      }
    }
    mavenCentral()
  }
}

include(":app:common", ":app:android", ":app:desktop")

include(":core:authentication", ":core:designsystem", ":core:ui", ":core:utils")

include(":feature:onboarding", ":feature:home", ":feature:analytics", ":feature:profile")

// iOS and macOS targets are opt-in (-Pwalenje.apple=true, which CI, Xcode and
// scripts/macos-app.sh pass), so Android and desktop work skips Kotlin/Native. The macOS app has no
// other target, so it's only included with them.
if (providers.gradleProperty("walenje.apple").map(String::toBoolean).getOrElse(false)) {
  include(":app:macos")
}
