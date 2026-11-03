import org.gradle.accessors.dm.LibrariesForLibs

// Base for every Kotlin module: Kotlin Multiplatform, JDK 25 (as Tajji central) and ktfmt. Targets come from
// walenje-kmp-library or the app module.
plugins {
  id("org.jetbrains.kotlin.multiplatform")
  id("walenje-code-format")
}

val libs = the<LibrariesForLibs>()

kotlin {
  jvmToolchain(25)
  compilerOptions { freeCompilerArgs.add("-Xexpect-actual-classes") }
  sourceSets {
    commonTest.dependencies {
      implementation(kotlin("test"))
      implementation(libs.kotlinx.coroutines.test)
    }
  }
}
