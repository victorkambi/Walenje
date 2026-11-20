package convention

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal val Project.kotlin: KotlinMultiplatformExtension
    get() = extensions.getByType()

/** Configures Kotlin Multiplatform, as the `kotlin { }` block in a build script does. */
internal fun Project.kotlin(configure: KotlinMultiplatformExtension.() -> Unit) = kotlin.configure()

/**
 * Adds dependencies to a source set when it exists. Use it for platform source sets such as
 * `androidMain` or `jvmTest`: the target that creates them can come after this plugin.
 */
internal fun NamedDomainObjectContainer<KotlinSourceSet>.whenAdded(
    name: String,
    dependencies: KotlinDependencyHandler.() -> Unit,
) {
    matching { it.name == name }.configureEach { dependencies(dependencies) }
}
