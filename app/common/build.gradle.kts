import convention.iosTargets

// Shared app entry: App(), navigation, Koin wiring, and the iOS framework the Xcode project embeds.
plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.compose)
    alias(libs.plugins.convention.serialization)
    alias(libs.plugins.convention.koin)
    alias(libs.plugins.convention.logging)
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
