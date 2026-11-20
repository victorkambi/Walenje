import org.gradle.accessors.dm.LibrariesForLibs

// ktfmt, kotlinlang style (4-space indent). `check` runs ktfmtCheck; nothing reformats during a
// build (run ktfmtFormat).
plugins { id("com.ncorti.ktfmt.gradle") }

val libs = the<LibrariesForLibs>()

ktfmt {
    kotlinLangStyle()
    removeUnusedImports = true
    maxWidth = 100
}

dependencies { "ktfmt"(libs.ktfmt) }
