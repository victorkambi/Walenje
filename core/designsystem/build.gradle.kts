plugins {
    alias(libs.plugins.walenje.library)
    alias(libs.plugins.walenje.compose)
}

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
