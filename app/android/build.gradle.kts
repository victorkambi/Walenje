import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("com.android.application")
  id("walenje-format-code")
}

android {
  namespace = "kambi.victor.walenje"

  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "kambi.victor.walenje"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
  }

  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }

  buildTypes {
    release { isMinifyEnabled = true }
    debug { isMinifyEnabled = false }
  }
}

tasks.withType<KotlinCompile>().configureEach {
  compilerOptions { jvmTarget.set(JvmTarget.JVM_21) }
}

tasks.withType<JavaCompile>().configureEach {
  sourceCompatibility = JvmTarget.JVM_21.toString()
  targetCompatibility = JvmTarget.JVM_21.toString()
}

dependencies {
  implementation(projects.app.common)
  implementation(projects.core.authentication)

  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.appcompat)
}
