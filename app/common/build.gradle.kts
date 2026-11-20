// Shared app entry: App(), navigation, Koin wiring, and the iOS framework the Xcode project embeds.
plugins {
    id("walenje.library")
    id("walenje.serialization")
    id("walenje.koin")
    id("walenje.logging")
}

kotlin {
    iosTargets {
        binaries.framework {
            baseName = "common"
            isStatic = true
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
            implementation(libs.compose.navigation3.ui)
            implementation(libs.jetbrains.lifecycle.viewmodel.navigation3)
        }
    }
}
