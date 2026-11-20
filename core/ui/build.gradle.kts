plugins {
    id("walenje.library")
    id("walenje.koin")
    id("walenje.logging")
}

kotlin { sourceSets { commonMain.dependencies { api(projects.core.designsystem) } } }
