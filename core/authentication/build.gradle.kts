plugins {
    alias(libs.plugins.walenje.library)
    alias(libs.plugins.walenje.logging)
}

kotlin { sourceSets { androidMain.dependencies { implementation(libs.androidx.biometric) } } }
