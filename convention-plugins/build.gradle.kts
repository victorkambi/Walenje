plugins {
    `kotlin-dsl`
    alias(libs.plugins.ktfmt)
}

dependencies {
    implementation(libs.plugins.kotlinMultiplatform.toDep())
    implementation(libs.plugins.androidApplication.toDep())
    implementation(libs.plugins.androidKmpLibrary.toDep())
    implementation(libs.plugins.composeCompiler.toDep())
    implementation(libs.plugins.composeMultiplatform.toDep())
    implementation(libs.plugins.kotlinx.serialization.toDep())
    implementation(libs.plugins.ktfmt.toDep())

    // Makes the generated type-safe `libs` accessors visible to the plugin classes (Gradle issue
    // 15383).
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

kotlin { jvmToolchain(25) }

// Each id also has a `[plugins]` entry in libs.versions.toml, so modules apply them with
// `alias(libs.plugins.convention.<name>)`.
gradlePlugin {
    plugins {
        fun convention(id: String, className: String) {
            register(id) {
                this.id = id
                implementationClass = "convention.$className"
            }
        }
        // Roles: one per module.
        convention("convention.android-app", "AndroidAppConventionPlugin")
        convention("convention.library", "LibraryConventionPlugin")
        convention("convention.feature", "FeatureConventionPlugin")
        // Capabilities: opt-in on top of a role.
        convention("convention.compose", "ComposeConventionPlugin")
        convention("convention.koin", "KoinConventionPlugin")
        convention("convention.serialization", "SerializationConventionPlugin")
        convention("convention.logging", "LoggingConventionPlugin")
        // Shared bases.
        convention("convention.multiplatform", "MultiplatformConventionPlugin")
        convention("convention.formatting", "FormattingConventionPlugin")
    }
}

ktfmt {
    kotlinLangStyle()
    removeUnusedImports = true
    maxWidth = 100
}

dependencies { ktfmt(libs.ktfmt) }

fun Provider<PluginDependency>.toDep() = map {
    "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}"
}
