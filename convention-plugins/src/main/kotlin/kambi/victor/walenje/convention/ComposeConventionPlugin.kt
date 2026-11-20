package kambi.victor.walenje.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure

/**
 * `walenje.compose`: Compose Multiplatform, its common libraries and Compose UI tests on the JVM.
 * In an Android library it also turns on Android resources, which Compose resources need.
 */
class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            apply<MultiplatformConventionPlugin>()
            apply(libs.plugins.composeCompiler)
            apply(libs.plugins.composeMultiplatform)

            kotlin {
                sourceSets.commonMain.dependencies {
                    implementation(libs.compose.foundation)
                    implementation(libs.compose.material3)
                    implementation(libs.compose.runtime)
                    implementation(libs.compose.ui)
                    implementation(libs.jetbrains.lifecycle.runtime)
                    implementation(libs.jetbrains.lifecycle.runtime.compose)
                    implementation(libs.jetbrains.lifecycle.viewmodel)
                }
                // Compose UI tests run on the JVM only: in commonTest they would also run as
                // Android host tests, which need Robolectric.
                sourceSets.whenAdded("jvmTest") {
                    implementation(libs.compose.ui.test)
                    implementation(composeDesktopCurrentOs)
                }
            }

            pluginManager.withPlugin(libs.plugins.androidKmpLibrary.get().pluginId) {
                (kotlin as ExtensionAware).extensions.configure<
                    KotlinMultiplatformAndroidLibraryExtension,
                > {
                    androidResources { enable = true }
                }
            }
        }
}
