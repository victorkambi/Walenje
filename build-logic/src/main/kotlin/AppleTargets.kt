import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

/** Whether iOS and macOS targets are enabled: `-Pwalenje.apple=true` (see settings.gradle.kts). */
val Project.appleTargetsEnabled: Boolean
  get() = providers.gradleProperty("walenje.apple").map(String::toBoolean).getOrElse(false)

/**
 * Configures each Apple target, e.g. with an app's `binaries.framework`. Apple targets exist only with
 * `-Pwalenje.apple=true`, so without it this does nothing.
 */
fun KotlinMultiplatformExtension.appleTargets(configure: KotlinNativeTarget.() -> Unit) {
  targets.withType(KotlinNativeTarget::class.java).configureEach(configure)
}
