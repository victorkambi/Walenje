plugins {
    alias(libs.plugins.walenje.library)
    alias(libs.plugins.walenje.compose)
    alias(libs.plugins.walenje.koin)
    alias(libs.plugins.walenje.logging)
}

kotlin { sourceSets { commonMain.dependencies { api(projects.core.designsystem) } } }
