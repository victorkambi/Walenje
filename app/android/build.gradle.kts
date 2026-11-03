plugins { id("walenje-android-application") }

android {
  namespace = "kambi.victor.walenje"
  defaultConfig {
    applicationId = "kambi.victor.walenje"
    versionCode = 1
    versionName = "1.0"
  }
  buildTypes { release { isMinifyEnabled = true } }
}

dependencies {
  implementation(projects.app.common)
  implementation(projects.core.authentication)
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.appcompat)
}
