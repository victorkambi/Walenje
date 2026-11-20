import kambi.victor.walenje.convention.iosTargets

// Shared app entry: App(), navigation, Koin wiring, and the iOS framework the Xcode project embeds.
plugins {
    alias(libs.plugins.walenje.library)
    alias(libs.plugins.walenje.compose)
    alias(libs.plugins.walenje.serialization)
    alias(libs.plugins.walenje.koin)
    alias(libs.plugins.walenje.logging)
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
