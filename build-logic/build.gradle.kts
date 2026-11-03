plugins { `kotlin-dsl` }

dependencies {
  implementation(libs.plugins.kotlinMultiplatform.toDep())
  implementation(libs.plugins.androidApplication.toDep())
  implementation(libs.plugins.androidKmpLibrary.toDep())
  implementation(libs.plugins.composeCompiler.toDep())
  implementation(libs.plugins.composeMultiplatform.toDep())
  implementation(libs.plugins.kotlinx.serialization.toDep())
  implementation(libs.plugins.ktfmt.toDep())

  // Makes the generated `libs` accessors visible to precompiled script plugins.
  implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}

kotlin { jvmToolchain(25) }

fun Provider<PluginDependency>.toDep() = map { "${it.pluginId}:${it.pluginId}.gradle.plugin:${it.version}" }
