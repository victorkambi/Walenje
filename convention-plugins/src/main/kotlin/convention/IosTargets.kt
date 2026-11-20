package convention

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

/**
 * Whether iOS targets are enabled: `-Pwalenje.ios=true` (CI and the Xcode build phase pass it).
 * Without it Gradle skips Kotlin/Native, so Android and desktop work stays fast.
 */
val Project.iosTargetsEnabled: Boolean
    get() = providers.gradleProperty("walenje.ios").map(String::toBoolean).getOrElse(false)

/**
 * Configures each iOS target, e.g. with an app's `binaries.framework`. iOS targets exist only with
 * `-Pwalenje.ios=true`, so without it this does nothing.
 */
fun KotlinMultiplatformExtension.iosTargets(configure: KotlinNativeTarget.() -> Unit) {
    targets.withType(KotlinNativeTarget::class.java).configureEach(configure)
}
