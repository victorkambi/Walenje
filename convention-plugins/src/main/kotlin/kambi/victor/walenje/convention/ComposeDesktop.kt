package kambi.victor.walenje.convention

import org.gradle.api.Project

/**
 * The Compose Desktop artifact for this machine, with its native Skiko runtime: for example
 * `org.jetbrains.compose.desktop:desktop-jvm-macos-arm64:<version>`. It replaces the deprecated
 * `compose.desktop.currentOs`. Group and version come from `libs.compose.desktop`.
 */
val Project.composeDesktopCurrentOs: String
    get() {
        val desktop = libs.compose.desktop.get()
        val version = desktop.versionConstraint.requiredVersion
        return "${desktop.module.group}:${desktop.module.name}-jvm-${hostOs()}-${hostArch()}:$version"
    }

private fun hostOs(): String {
    val name = System.getProperty("os.name")
    return when {
        name.startsWith("Mac", ignoreCase = true) -> "macos"
        name.startsWith("Windows", ignoreCase = true) -> "windows"
        name.startsWith("Linux", ignoreCase = true) -> "linux"
        else -> error("Compose Desktop doesn't support the OS '$name'")
    }
}

private fun hostArch(): String =
    when (val arch = System.getProperty("os.arch")) {
        "aarch64",
        "arm64" -> "arm64"
        "x86_64",
        "amd64" -> "x64"
        else -> error("Compose Desktop doesn't support the CPU architecture '$arch'")
    }
