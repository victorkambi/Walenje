# Build tool migrations

## Kotlin Toolchain → Gradle (2026-11-04)

Walenje is back on Gradle. Measured on the development Mac, Kotlin Toolchain 0.13.0 cost about 6.3 GB for this
project alone (its own Kotlin/Native compiler, two JDKs, a separate Maven cache, a download cache, and an internal
Gradle 9.5.0 for Android) and could share none of it with the Gradle caches the Tajji repositories need. Gradle
with Tajji central's exact versions reuses `~/.gradle/caches/9.8.2`, `~/.konan` and the JetBrains Runtime 25 JDK.

| Area | Now |
|---|---|
| Versions | Gradle 9.8.2, AGP 9.5.0, Kotlin 2.4.21, Compose 1.12.2: the same as central |
| Build logic | Flat `build-logic` included build (as central) instead of the old nested `convention-plugins/conventions` |
| JDK | Daemon and toolchain on JetBrains Runtime 25 (central's `gradle-daemon-jvm.properties`) |
| Apple targets | Opt-in with `-Pwalenje.apple=true`, like central's `tajji.ios` |
| Formatting | ktfmt through `com.ncorti.ktfmt.gradle`, pinned to ktfmt 0.64. Check only: the old build ran `ktfmtFormat` on every `build`. Central uses ktlint; Walenje keeps ktfmt because ktlint contradicts its Google style |
| Kept from the Toolchain period | native macOS app (now bundled by `:app:macos:assembleMacosApp{Debug,Release}`), `commonTest` tests on every platform, CI, Touch ID biometrics, separate `app/desktop` module (with Hot Reload and installers again) |
| iOS | `app/ios/Walenje.xcodeproj` restored; the build phase runs `embedAndSignAppleFrameworkForXcode`, the framework is `common` again |

A `gradle-best-practices` audit of the result led to: the wrapper pins `distributionSha256Sum` (the restored
`gradle-wrapper.jar` is byte-identical to Gradle 9.8.2's), repositories are declared in settings with content
filters and `FAIL_ON_PROJECT_REPOS`, catalog aliases name their libraries (`compose-ui`, `kermit`, ...), and two
redundant dependencies are gone. Accepted: nested project paths (`:core:ui`) create empty `:app`, `:core` and
`:feature` projects, as in central, because flat names would change every `projects.*` accessor and the
path-derived namespaces; `:app:macos` refers to the Compose plugin's `macosArm64AggregateResources` task by name
because the plugin exposes no typed handle.

Known: `./gradlew :app:android:tasks --all` fails because AGP 9.5 registers Compose Preview tasks that can't be
configured for this app. Builds never create them. The ktfmt plugin calls `Configuration.setVisible`, which
Gradle 11 removes.

## Gradle → Kotlin Toolchain (2026-11-03)

Walenje moves from Gradle (AGP 9.5, Gradle 9.8.2, convention plugins in `convention-plugins/`) to
[Kotlin Toolchain](https://kotlin-toolchain.org/) 0.13.0, using the `kotlin-tooling-gradle-to-kotlin-toolchain-project`
skill. Gradle baseline: commit `2c99a4a` on `main` (all targets green, iOS and desktop smoke-tested).

### Feasibility (checked against the 0.13.0 docs and a scratch `kotlin init`)

| Need | Toolchain | Result |
|---|---|---|
| KMP lib: android, iosArm64, iosSimulatorArm64, jvm | `kmp/lib` with `platforms:` | Supported |
| Compose Multiplatform + resources (`Res`, Montserrat font) | `settings.compose`, `composeResources/` | Supported. `resources.packageName` keeps the Gradle package `walenje.core.designsystem.generated.resources` |
| Android app consuming the shared lib | `android/app` | Supported |
| iOS app (SwiftUI host) | `ios/app` + `module.xcodeproj` | Supported. The framework is named `KotlinModules` instead of `common`, so `ContentView.swift` changes its import |
| Desktop app | `jvm/app` | Supported for run and executable JAR |
| Desktop installers (Dmg/Msi/Deb via `nativeDistributions`) | none | **Dropped**: no Toolchain equivalent; never used in CI or releases |
| Compose Hot Reload | `./kotlin run --compose-hot-reload` | Supported (new; the Gradle plugin was declared but never applied) |

### Inventory

#### Plugins

| Gradle plugin | Decision |
|---|---|
| `org.jetbrains.kotlin.multiplatform` | Native: `product: kmp/lib` |
| `com.android.kotlin.multiplatform.library` | Native: `android` platform + `settings.android` |
| `com.android.application` | Native: `product: android/app` |
| `org.jetbrains.compose`, `org.jetbrains.kotlin.plugin.compose` | Native: `settings.compose` |
| `org.jetbrains.kotlin.plugin.serialization` | Native: `settings.kotlin.serialization` |
| `com.ncorti.ktfmt.gradle` (`walenje-format-code`) | Vendored local plugin `plugins/ktfmt` from [Heapy/ktc-ktfmt](https://github.com/Heapy/ktc-ktfmt) @ `39432a5` |
| `org.jlleitschuh.gradle.ktlint` | **Dropped.** Gradle applied it only to the root project, so it never checked module sources. A vendored ktlint plugin ([Heapy/ktc-ktlint](https://github.com/Heapy/ktc-ktlint)) reported 99 violations, many of them contradicting ktfmt's Google style (call-site trailing commas, indentation). ktfmt is the single formatter; `.editorconfig` stays for the IDE |
| `ktlintRuleset(compose-lint-checks)` | Dropped: it's an Android Lint artifact, so it never ran as a ktlint ruleset |
| `org.gradle.toolchains.foojay-resolver-convention`, daemon JVM criteria | Dropped: the Toolchain provisions its JDK |
| `TYPESAFE_PROJECT_ACCESSORS` | `//path/to/module` dependencies |

#### Convention plugins → templates (`templates/`)

| Convention | Template | Used by |
|---|---|---|
| `walenje-kmp`, `walenje-format-code`, compose half of `walenje-cmp-base` | `base` (Kotlin, JVM release, Compose settings, ktfmt) | every module |
| `walenje-kmp-library` (Android part) | `android` (compileSdk 37, minSdk 24, build tools) | KMP libs, Android app |
| `walenje-cmp-base` (dependencies) | `cmp` | KMP libs |
| `walenje-kotlinx` | `kotlinx` | utils, features, common |
| `walenje-koin` + `[bundles] koin` | `koin` | ui, features, common |
| `walenje-logger` | `logger` | all KMP libs except designsystem |
| `walenje-application-feature` | `feature` | features |

#### Modules

| Gradle | Toolchain | Notes |
|---|---|---|
| `:app:common` | `app/common` (`kmp/lib`) | Desktop `main.kt` and the desktop-only deps move to `app/desktop`; launcher `res/` moves to `app/android` |
| `:app:android` | `app/android` (`android/app`) | `src/main/kotlin/walenje/*` → `src/`, manifest → `src/AndroidManifest.xml` |
| (inside `:app:common` jvm) | `app/desktop` (`jvm/app`) | New module |
| `app/ios` (Xcode project) | `app/ios` (`ios/app`) | Swift and assets → `src/`, new `module.xcodeproj` (bundle id `kambi.victor.Walenje`, iOS 26.2), self-contained `Info.plist` |
| `:core:*`, `:feature:*` | same paths (`kmp/lib`) | `commonMain/kotlin` → `src/`, `androidMain/kotlin` → `src@android/`, `iosMain/kotlin` → `src@ios/`, `jvmMain/kotlin` → `src@jvm/`, `commonMain/composeResources` → `composeResources/` |

#### Other Gradle-only config that drops

`gradle.properties` (`org.gradle.*`, `android.*`, `kotlin.*` flags), `local.properties` (stays untracked), the
`gradle/wrapper/`, `gradlew*` and the `[plugins]`/`[bundles]` catalog sections. There is no CI to rewrite.
Afterwards the catalog moved from `gradle/libs.versions.toml` to the root `libs.versions.toml`, and the
`gradle-skills` plugin was disabled. The Toolchain still runs Gradle 9.5.0 internally for Android builds, using
`~/.gradle`; that's expected and needs no project files.

### Test plan

- [x] `./kotlin show modules` lists all 13 modules (12 app modules + `ktfmt`)
- [x] `./kotlin build -p jvm -p android` succeeds (Android APK and desktop JAR)
- [x] `./kotlin build` succeeds for every platform, including iosArm64 and iosSimulatorArm64 (after
  `xcodebuild -runFirstLaunch` finished the Xcode 27 first-launch setup)
- [x] `./kotlin check ktfmtCheck` passes on every module, and fails when a file is misformatted
- [x] `./kotlin show dependencies` matches the Gradle runtime classpaths (`androidRuntimeClasspath`,
  `jvmRuntimeClasspath`, iOS klibs) for every module, except:
  - desktop-only libraries (`compose.desktop.currentOs`, coroutines-swing) moved from `app/common` to `app/desktop`
  - the Toolchain adds `kotlin-stdlib-jdk7/8` and, with Compose enabled, `components-resources`
  - iOS resolves `material-ripple` 1.12.0-beta01 (from Material 3 1.12.0-alpha03) instead of 1.12.2, because the
    Toolchain has no equivalent of the Compose Gradle plugin's strict version alignment
- [x] Desktop app runs (`./kotlin run -m desktop`, with Compose Hot Reload) with no exceptions
- [x] iOS app (Toolchain-built `Walenje.app`, bundle id `kambi.victor.Walenje`) runs in the iPhone 17 Pro
  simulator: Montserrat and icons render from Compose resources; PIN entry → confirmation → Home works
- [ ] Android app on an emulator: the AVD needs more free disk than is available; APK builds
