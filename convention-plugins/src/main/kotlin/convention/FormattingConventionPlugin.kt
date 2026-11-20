package convention

import com.ncorti.ktfmt.gradle.KtfmtExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * `convention.formatting`: ktfmt, kotlinlang style (4-space indent). `check` runs `ktfmtCheck`;
 * nothing reformats during a build, so run `ktfmtFormat`. `convention-plugins/build.gradle.kts`
 * repeats these settings because an included build can't apply its own plugins.
 */
class FormattingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project): Unit =
        with(target) {
            apply(libs.plugins.ktfmt)

            extensions.configure<KtfmtExtension> {
                kotlinLangStyle()
                removeUnusedImports.set(true)
                maxWidth.set(100)
            }

            dependencies { "ktfmt"(libs.ktfmt) }
        }
}
