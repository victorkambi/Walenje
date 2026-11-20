plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.serialization)
    alias(libs.plugins.convention.logging)
}

// BaseViewModel extends ViewModel, so callers need the type.
kotlin { sourceSets { commonMain.dependencies { api(libs.jetbrains.lifecycle.viewmodel) } } }
