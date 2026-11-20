package kambi.victor.walenje.convention

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * `walenje.feature`: a feature module. Screens, ViewModels and a Koin module on top of `core:ui`
 * and `core:utils`. Add other core modules in the feature's own build script.
 */
class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            apply<LibraryConventionPlugin>()
            apply<ComposeConventionPlugin>()
            apply<SerializationConventionPlugin>()
            apply<KoinConventionPlugin>()
            apply<LoggingConventionPlugin>()

            kotlin {
                sourceSets.commonMain.dependencies {
                    api(project(":core:ui"))
                    api(project(":core:utils"))
                }
            }
        }
}
