package convention

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * `convention.multiplatform`: the base for every Kotlin module. Kotlin Multiplatform, the JDK,
 * `-Xexpect-actual-classes`, test libraries and ktfmt. It adds no targets:
 * [LibraryConventionPlugin] or the app module does that.
 */
class MultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            apply(libs.plugins.kotlinMultiplatform)
            apply<FormattingConventionPlugin>()

            kotlin {
                jvmToolchain(WalenjeBuild.JDK)
                compilerOptions { freeCompilerArgs.add("-Xexpect-actual-classes") }
                sourceSets.commonTest.dependencies {
                    implementation(kotlin("test"))
                    implementation(libs.kotlinx.coroutines.test)
                }
            }
        }
}
