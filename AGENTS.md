# AGENTS.md

Guidance for coding agents working in this repository. Read `README.md` for the product overview.

## What this is

Walenje is a personal finance tracker: a Kotlin Multiplatform app with a Compose Multiplatform UI that targets
Android, iOS, macOS (native) and desktop (JVM). It is in early development. Onboarding (welcome, secure-wallet explainer, PIN setup)
and biometrics work; Home, Analytics and Profile are placeholders.

## Modules

| Path | Role |
|---|---|
| `app/common` | Shared app entry (`App.kt`), navigation graph, Koin wiring and iOS `MainViewController` |
| `app/android` | Android application (`MainActivity`, `MainApplication`, manifest, launcher resources) |
| `app/desktop` | Desktop application (`main.kt`, Compose Desktop packaging) |
| `app/ios` | SwiftUI host (`Walenje.xcodeproj`); its build phase runs Gradle and imports the shared `common` framework |
| `app/macos` | Native macOS app (Kotlin/Native + Compose for macOS, `main.kt`); `assembleMacosAppDebug` bundles it |
| `core/authentication` | `Biometrics` expect/actual per platform |
| `core/designsystem` | Material 3 theme, Montserrat typography, icons, shared components |
| `core/ui` | Shared UI pieces such as the PIN keypad and its ViewModel |
| `core/utils` | MVI base classes (`mvi/`), UI state types |
| `feature/onboarding` | Welcome, secure-wallet and PIN setup screens (package `feature.welcome`) |
| `feature/home`, `feature/analytics`, `feature/profile` | Placeholders |

Packages live under `kambi.victor.walenje`.

## Build

The build uses Gradle 9.8.2 with AGP 9.5 (built-in Kotlin, `com.android.kotlin.multiplatform.library`). Gradle, AGP,
Kotlin and Compose versions match Tajji central (`~/work/tajji/central`) so both builds share `~/.gradle` and
`~/.konan`; bump them together. `MIGRATION_PLAN.md` records the move from Gradle to Kotlin Toolchain and back.

- `settings.gradle.kts` lists the modules and the repositories (content-filtered; projects can't add their own).
- Shared configuration lives in convention plugins in `build-logic/src/main/kotlin/`:
  - `walenje-kmp`: Kotlin Multiplatform, JDK 25 toolchain, `-Xexpect-actual-classes`, `kotlin.test` and
    `kotlinx-coroutines-test` for every module's tests, ktfmt
  - `walenje-cmp`: Compose Multiplatform and its common dependencies
  - `walenje-kmp-library`: a shared library: Android (namespace from the project path), JVM, and the Apple targets
  - `walenje-feature`: a feature module (library + kotlinx + Koin + Kermit + the core modules)
  - `walenje-kotlinx`, `walenje-koin`, `walenje-logger`, `walenje-android-application`, `walenje-code-format`

  Change a setting in the convention plugin, not in each module.
- iOS and macOS targets are opt-in: `-Pwalenje.apple=true` (CI, the Xcode build phase and `scripts/macos-app.sh`
  pass it). Without it Gradle skips Kotlin/Native entirely and `:app:macos` isn't included.
- Versions live only in `gradle/libs.versions.toml`. Reference them as `libs.<alias>`; never write Maven
  coordinates into a build script.
- Sources follow the Kotlin Multiplatform layout: `src/commonMain/kotlin`, `src/<platform>Main/kotlin`,
  `src/commonTest/kotlin`, and Compose resources in `src/commonMain/composeResources`.
- The Gradle daemon runs on JetBrains Runtime 25 (`gradle/gradle-daemon-jvm.properties`, as central). Upgrade the
  wrapper with `./gradlew wrapper --gradle-version <v> --gradle-distribution-sha256-sum <sha>`, never by hand.

## Architecture rules

- **MVI.** Each screen has a contract interface with `Event : UiEvent`, `State : UiState` and `Effect : UiEffect`
  (see `feature/onboarding/.../SetPinContract.kt`). Its ViewModel extends
  `core/utils/.../mvi/BaseViewModel` and changes state only through `setState`, `setEvent` and `setEffect`.
- **DI.** Use Koin. Each feature exposes a `module { }` (for example `welcomeModules`) and registers ViewModels with
  `viewModelOf`. The app-level configuration is `createKoinConfiguration()` in `app/common/.../di/`.
- **Navigation.** Type-safe `@Serializable` routes in `app/common/.../navigation/Navigation.kt`. Screens take
  navigation callbacks; they never receive a `NavController`.
- **UI.** Shared UI lives in common `src/` and goes through `core/designsystem` (theme, `Text`, `Button`, icons).
  Don't hard-code colours, fonts or sizes in feature code.
- **Platform code.** Keep Android, iOS, macOS and JVM APIs in `src/androidMain/`, `src/iosMain/`, `src/macosMain/`
  and `src/jvmMain/` behind `expect`/`actual`. Every shared module targets all five platforms, so a new `expect` needs an
  `actual` for each. Never put `Context` or other Android types in common `src/`.
- **Logging.** Use Kermit through the module's `internal val logger`. Don't use `println`.

## Code style

- ktfmt, Google style, max width 100, unused imports removed. `ktfmtCheck` checks it and nothing reformats during a
  build, so run `./gradlew ktfmtFormat` before committing. `@Composable` functions are
  PascalCase. ktlint is not used: it disagrees with ktfmt's output.
- Two-space indentation, trailing commas allowed.

## Commands

Set `ANDROID_HOME` (for example `~/Library/Android/sdk`). iOS needs Xcode with its first-launch setup done
(`sudo xcodebuild -runFirstLaunch`). Build the tasks you need rather than `./gradlew build`, which also links
release binaries for every Apple target.

```sh
./gradlew :app:android:assembleDebug          # Android APK (installDebug to put it on a device or emulator)
./gradlew :app:desktop:run                    # desktop app (:app:desktop:hotRunJvm for Compose Hot Reload)
./gradlew jvmTest testAndroidHostTest         # tests on JVM and Android (fast loop)
./gradlew :feature:onboarding:jvmTest         # one module's tests
./gradlew -Pwalenje.apple=true iosSimulatorArm64Test macosArm64Test   # tests on the iOS simulator and macOS
./gradlew ktfmtCheck                          # formatting check (ktfmtFormat to fix; add -Pwalenje.apple=true
                                              # to cover iOS and macOS sources)
scripts/macos-app.sh --open                   # native macOS app as app/macos/build/macos/Walenje.app
./gradlew :app:desktop:packageDmg             # desktop installer (packageMsi / packageDeb on those systems)
./cleanup.sh                                  # build output, Xcode user data, Kotlin/Native temp leftovers
./cleanup.sh --ide --deep                     # also .idea, the daemons and the project's .gradle
```

For iOS, open `app/ios/Walenje.xcodeproj` in Xcode and run the `Walenje` scheme on a simulator; its build phase
runs `./gradlew -Pwalenje.apple=true :app:common:embedAndSignAppleFrameworkForXcode`. The desktop installers come
from Compose's `nativeDistributions` and aren't built in CI.

## Tests and CI

- Tests live in each module's `src/commonTest/kotlin`, so they run on JVM, Android (host tests), the iOS simulator
  and macOS, and use `kotlin.test`. Current coverage: `BaseViewModel` (`core/utils`), `SetPinViewModel`
  (`core/ui`) and `SetPinScreenViewModel` (`feature/onboarding`).
- ViewModels run on `viewModelScope`, so tests call `Dispatchers.setMain(UnconfinedTestDispatcher())` and use
  `runTest` from `kotlinx-coroutines-test` (every module's tests have it through `walenje-kmp`). Collect effects
  with `effect.take(n).toList()`.
- New business logic (ViewModels, use cases, repositories) gets tests in the same change.
- `.github/workflows/ci.yml` runs on `macos-26` for every push to `main` and every pull request: the Android APK,
  desktop JAR, iOS simulator framework and macOS app bundle, the tests on all four platforms, and `ktfmtCheck`. It
  doesn't build the iOS app itself: Xcode can need a multi-GB iOS platform download on the runner. Keep it green.

## Git

- `main` is the default branch. Work on a feature branch.
- Conventional Commits: `type(scope): subject`, for example `build(deps): bump kotlin to 2.4.0`.
- Don't commit unrelated working-tree changes.
- To land a branch: `git fetch origin`, rebase the branch onto `origin/main`, fast-forward local `main` to
  `origin/main` and then to the branch, push `main` to `origin`, and delete the local branch. There are no pull
  requests.
