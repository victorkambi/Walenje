plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.compose)
    alias(libs.plugins.convention.koin)
    alias(libs.plugins.convention.logging)
}

kotlin { sourceSets { commonMain.dependencies { api(projects.core.designsystem) } } }
