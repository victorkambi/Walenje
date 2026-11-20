import convention.composeDesktopCurrentOs

// Desktop (JVM) dev app for Compose Hot Reload: ./gradlew :app:desktop:hotRunJvm. It isn't a
// shipped target, so it has no installer packaging.
plugins {
    alias(libs.plugins.convention.compose)
    id("org.jetbrains.compose.hot-reload")
}

kotlin {
    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(projects.app.common)
            implementation(projects.core.authentication)
            implementation(composeDesktopCurrentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

compose.desktop { application { mainClass = "kambi.victor.walenje.MainKt" } }
