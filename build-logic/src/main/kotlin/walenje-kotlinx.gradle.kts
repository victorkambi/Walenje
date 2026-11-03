import org.gradle.accessors.dm.LibrariesForLibs

plugins {
  id("walenje-kmp")
  id("org.jetbrains.kotlin.plugin.serialization")
}

val libs = the<LibrariesForLibs>()

kotlin { sourceSets { commonMain.dependencies { implementation(libs.kotlinx.serialization.json) } } }
