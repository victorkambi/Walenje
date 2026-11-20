plugins {
    alias(libs.plugins.convention.library)
    alias(libs.plugins.convention.logging)
}

kotlin { sourceSets { androidMain.dependencies { implementation(libs.androidx.biometric) } } }
