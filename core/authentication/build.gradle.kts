plugins {
  id("walenje-kmp-library")
  id("walenje-logger")
}

kotlin { sourceSets { androidMain.dependencies { implementation(libs.androidx.biometric) } } }
