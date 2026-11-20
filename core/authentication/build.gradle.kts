plugins {
    id("walenje.library")
    id("walenje.logging")
}

kotlin { sourceSets { androidMain.dependencies { implementation(libs.androidx.biometric) } } }
