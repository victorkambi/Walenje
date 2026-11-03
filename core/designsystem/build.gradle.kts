plugins { id("walenje-kmp-library") }

kotlin {
  sourceSets {
    commonMain.dependencies {
      api(libs.compose.components.resources)
      api(libs.graphics.shapes)
    }
  }
}

compose.resources {
  packageOfResClass = "walenje.core.designsystem.generated.resources"
  generateResClass = always
}
