import org.gradle.accessors.dm.LibrariesForLibs

plugins { id("walenje-kmp") }

val libs = the<LibrariesForLibs>()

kotlin {
  sourceSets {
    commonMain.dependencies {
      implementation(project.dependencies.platform(libs.koin.bom))
      implementation(libs.bundles.koin)
    }
    androidMain.dependencies { implementation(libs.koin.android) }
  }
}
