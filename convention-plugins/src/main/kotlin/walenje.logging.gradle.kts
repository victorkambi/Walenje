import org.gradle.accessors.dm.LibrariesForLibs

plugins { id("walenje.multiplatform") }

val libs = the<LibrariesForLibs>()

kotlin { sourceSets { commonMain.dependencies { api(libs.kermit) } } }
