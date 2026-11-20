package kambi.victor.walenje.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

/** `walenje.koin`: Koin core, Compose and ViewModel support, plus `koin-android` on Android. */
class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            apply<MultiplatformConventionPlugin>()

            kotlin {
                sourceSets.commonMain.dependencies {
                    implementation(project.dependencies.platform(libs.koin.bom))
                    implementation(libs.bundles.koin)
                }
                sourceSets.whenAdded("androidMain") { implementation(libs.koin.android) }
            }
        }
}

/** `walenje.serialization`: the kotlinx.serialization compiler plugin and its JSON library. */
class SerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            apply<MultiplatformConventionPlugin>()
            apply(libs.plugins.kotlinx.serialization)

            kotlin {
                sourceSets.commonMain.dependencies {
                    implementation(libs.kotlinx.serialization.json)
                }
            }
        }
}

/** `walenje.logging`: Kermit, exposed as `api` so callers can use the module's logger types. */
class LoggingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            apply<MultiplatformConventionPlugin>()

            kotlin { sourceSets.commonMain.dependencies { api(libs.kermit) } }
        }
}
