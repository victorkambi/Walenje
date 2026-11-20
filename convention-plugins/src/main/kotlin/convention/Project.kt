package convention

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.the
import org.gradle.plugin.use.PluginDependency

/** Build-wide constants. Change them here, not in the plugins. */
internal object WalenjeBuild {
    /** JDK for compilation and the Android bytecode level. Matches Tajji central. */
    const val JDK = 25

    /** Root package. Android namespaces append the module path to it. */
    const val NAMESPACE = "kambi.victor.walenje"
}

/**
 * The type-safe `libs` catalog. The accessors are on the classpath through the
 * `files(libs.javaClass...)` dependency in `convention-plugins/build.gradle.kts` (Gradle issue
 * 15383).
 */
internal val Project.libs: LibrariesForLibs
    get() = the()

/** Applies a plugin from the version catalog's `[plugins]` table. */
internal fun Project.apply(plugin: Provider<PluginDependency>) {
    pluginManager.apply(plugin.get().pluginId)
}

/** Applies another Walenje convention plugin. Gradle applies each plugin once per project. */
internal inline fun <reified T : Plugin<Project>> Project.apply() {
    pluginManager.apply(T::class.java)
}

/** The module's Android namespace: `:feature:home` becomes `kambi.victor.walenje.feature.home`. */
internal val Project.androidNamespace: String
    get() = WalenjeBuild.NAMESPACE + path.replace(':', '.')
