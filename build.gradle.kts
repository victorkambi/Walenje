plugins {
  // Declared once here so every module shares one classloader for these plugins.
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.androidKmpLibrary) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  alias(libs.plugins.composeMultiplatform) apply false
  alias(libs.plugins.composeCompiler) apply false
  alias(libs.plugins.composeHotReload) apply false
  alias(libs.plugins.kotlinx.serialization) apply false
  alias(libs.plugins.ktfmt) apply false
}
