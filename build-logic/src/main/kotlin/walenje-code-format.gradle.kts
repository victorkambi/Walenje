import org.gradle.accessors.dm.LibrariesForLibs

// ktfmt, Google style. `check` runs ktfmtCheck; nothing reformats during a build (run ktfmtFormat).
plugins { id("com.ncorti.ktfmt.gradle") }

val libs = the<LibrariesForLibs>()

ktfmt {
  googleStyle()
  removeUnusedImports = true
  maxWidth = 100
}

dependencies { "ktfmt"(libs.ktfmt) }
