package kambi.victor.walenje.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure

/**
 * `walenje.library`: a shared module for Android, iOS (with `-Pwalenje.ios=true`) and JVM. The JVM
 * target exists only for the desktop dev app (Compose Hot Reload) and fast tests; it isn't shipped.
 * It adds no Compose: apply [ComposeConventionPlugin] for UI.
 */
class LibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            apply<MultiplatformConventionPlugin>()
            apply(libs.plugins.androidKmpLibrary)

            kotlin {
                (this as ExtensionAware).extensions.configure<
                    KotlinMultiplatformAndroidLibraryExtension,
                > {
                    // AGP 9 needs a unique namespace per module.
                    namespace = androidNamespace
                    compileSdk = libs.versions.android.compileSdk.get().toInt()
                    minSdk = libs.versions.android.minSdk.get().toInt()
                    withHostTest {}
                }

                jvm()

                if (iosTargetsEnabled) {
                    iosArm64()
                    iosSimulatorArm64()
                }
            }
        }
}
