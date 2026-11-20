import org.gradle.accessors.dm.LibrariesForLibs

// A feature module: screens, ViewModels and a Koin module, on top of the core modules.
plugins {
    id("walenje.library")
    id("walenje.serialization")
    id("walenje.koin")
    id("walenje.logging")
}

val libs = the<LibrariesForLibs>()

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(project(":core:ui"))
            api(project(":core:utils"))
            api(project(":core:authentication"))
        }
    }
}
