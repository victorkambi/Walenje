import org.gradle.accessors.dm.LibrariesForLibs

// The Android app entry module. Uses AGP 9 built-in Kotlin: do not apply org.jetbrains.kotlin.android.
plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.plugin.compose")
  id("walenje-code-format")
}

val libs = the<LibrariesForLibs>()

android {
  compileSdk = libs.versions.android.compileSdk.get().toInt()
  defaultConfig {
    minSdk = libs.versions.android.minSdk.get().toInt()
    targetSdk = libs.versions.android.targetSdk.get().toInt()
  }
  buildFeatures { compose = true }
  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
  }
}

kotlin { jvmToolchain(25) }
