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
// `alias(libs.plugins.walenje.<name>)`.
gradlePlugin {
    plugins {
        fun convention(id: String, className: String) {
            register(id) {
                this.id = id
                implementationClass = "kambi.victor.walenje.convention.$className"
            }
        }
        // Roles: one per module.
        convention("walenje.android-app", "AndroidAppConventionPlugin")
        convention("walenje.library", "LibraryConventionPlugin")
        convention("walenje.feature", "FeatureConventionPlugin")
        // Capabilities: opt-in on top of a role.
        convention("walenje.compose", "ComposeConventionPlugin")
        convention("walenje.koin", "KoinConventionPlugin")
        convention("walenje.serialization", "SerializationConventionPlugin")
        convention("walenje.logging", "LoggingConventionPlugin")
        // Shared bases.
        convention("walenje.multiplatform", "MultiplatformConventionPlugin")
        convention("walenje.formatting", "FormattingConventionPlugin")
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
