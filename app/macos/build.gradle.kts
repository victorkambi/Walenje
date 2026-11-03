import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

// Native macOS app (Kotlin/Native, no JVM), included only with -Pwalenje.apple=true.
// assembleMacosAppDebug/Release build build/macos/Walenje.app; scripts/macos-app.sh runs one and
// opens it.
plugins { id("walenje-cmp") }

kotlin {
  macosArm64 {
    binaries.executable {
      baseName = "Walenje"
      entryPoint = "kambi.victor.walenje.main"
    }
  }

  sourceSets {
    macosMain.dependencies {
      implementation(projects.app.common)
      implementation(projects.core.authentication)
    }
  }
}

// Compose copies resources next to test executables but not app executables, and the macOS
// resource reader looks in <bundle>/Contents/Resources/compose-resources. So the bundle takes the
// resources Compose aggregates from every module, alongside the executable and Info.plist.
val executables = kotlin.macosArm64().binaries
val aggregatedResources = tasks.named("macosArm64AggregateResources")

for (buildType in listOf(NativeBuildType.DEBUG, NativeBuildType.RELEASE)) {
  val variant = buildType.name.lowercase().replaceFirstChar(Char::uppercase)
  val link = executables.getExecutable(buildType).linkTaskProvider
  tasks.register<Sync>("assembleMacosApp$variant") {
    group = "build"
    description = "Assembles the $variant macOS app bundle in build/macos/Walenje.app."
    into(layout.buildDirectory.dir("macos/Walenje.app"))
    into("Contents") { from("Info.plist") }
    into("Contents/MacOS") {
      from(link.flatMap { it.outputFile })
      rename { "Walenje" }
    }
    into("Contents/Resources/compose-resources") { from(aggregatedResources) }
  }
}
