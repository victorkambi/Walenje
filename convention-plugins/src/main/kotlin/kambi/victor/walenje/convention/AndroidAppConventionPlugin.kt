package kambi.victor.walenje.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

/**
 * `walenje.android-app`: the Android app entry module. Uses AGP 9 built-in Kotlin: do not apply
 * `org.jetbrains.kotlin.android`.
 */
class AndroidAppConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            apply(libs.plugins.androidApplication)
            apply(libs.plugins.composeCompiler)
            apply<FormattingConventionPlugin>()

            extensions.configure<ApplicationExtension> {
                compileSdk = libs.versions.android.compileSdk.get().toInt()
                defaultConfig {
                    minSdk = libs.versions.android.minSdk.get().toInt()
                    targetSdk = libs.versions.android.targetSdk.get().toInt()
                }
                buildFeatures { compose = true }
                packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
                compileOptions {
                    sourceCompatibility = JavaVersion.toVersion(WalenjeBuild.JDK)
                    targetCompatibility = JavaVersion.toVersion(WalenjeBuild.JDK)
                }
            }

            extensions.configure<KotlinProjectExtension> { jvmToolchain(WalenjeBuild.JDK) }
        }
}
