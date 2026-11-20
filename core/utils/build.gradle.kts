plugins {
    alias(libs.plugins.walenje.library)
    alias(libs.plugins.walenje.serialization)
    alias(libs.plugins.walenje.logging)
}

// BaseViewModel extends ViewModel, so callers need the type.
kotlin { sourceSets { commonMain.dependencies { api(libs.jetbrains.lifecycle.viewmodel) } } }
