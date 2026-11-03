plugins {
  id("walenje-kmp-library")
  id("walenje-koin")
  id("walenje-logger")
}

kotlin { sourceSets { commonMain.dependencies { api(projects.core.designsystem) } } }
