# AGENTS.md

Guidance for coding agents working in this repository. Read `PRODUCT.md` for the product (users, journeys,
roadmap), `ROADMAP.md` for the build plan and `DESIGN.md` for the visual direction.

## What this is

Walenje is a receipts app for freelancers and sole traders in Kenya: capture a receipt, extract and tag its data
on the device, and export records for accounting. It is local first (no account, no server) until `PRODUCT.md`
says otherwise. It is a Kotlin Multiplatform app with a Compose Multiplatform UI that targets Android and iOS, plus
a desktop (JVM) build used only for Compose Hot Reload during development. It is in early development. Onboarding
(welcome, secure-wallet explainer, PIN setup) and biometrics work; Home, Analytics and Profile are placeholders.
Phase 1 in `ROADMAP.md` adds the planned modules `core/receipts`, `core/database`, `core/scanner`, `core/export`,
`feature/capture` and `feature/receipts`, and replaces `feature/analytics` with `feature/reports` and
`feature/profile` with `feature/settings`.

## Modules

| Path | Role |
|---|---|
| `app/common` | Shared app entry (`App.kt`), navigation graph, Koin wiring and iOS `MainViewController` |
| `app/android` | Android application (`MainActivity`, `MainApplication`, manifest, launcher resources) |
| `app/desktop` | Desktop (JVM) dev app for Compose Hot Reload (`main.kt`); not shipped, no packaging |
| `app/ios` | SwiftUI host (`Walenje.xcodeproj`); its build phase runs Gradle and imports the shared `common` framework |
| `core/authentication` | `Biometrics` expect/actual per platform |
| `core/designsystem` | Material 3 theme, Inter Tight typography, icons, shared components |
| `core/ui` | Shared UI pieces such as the PIN keypad and its ViewModel |
| `core/utils` | MVI base classes (`mvi/`), UI state types |
| `feature/onboarding` | Welcome, secure-wallet and PIN setup screens (package `feature.welcome`) |
| `feature/home`, `feature/analytics`, `feature/profile` | Placeholders |

Packages live under `kambi.victor.walenje`.

## Build

The build uses Gradle 9.9.1 with AGP 9.5 (built-in Kotlin, `com.android.kotlin.multiplatform.library`). Gradle, AGP,
Kotlin and Compose versions match Tajji central (`~/work/tajji/central`) so both builds share `~/.gradle` and
`~/.konan`; bump them together. `MIGRATION_PLAN.md` records the move from Gradle to Kotlin Toolchain and back.

- `settings.gradle.kts` lists the modules and the repositories (content-filtered; projects can't add their own).
- Shared configuration lives in convention plugins: Kotlin classes in
  `convention-plugins/src/main/kotlin/kambi/victor/walenje/convention/`, registered in `convention-plugins/build.gradle.kts`.
  Each id has a `[plugins]` entry in `libs.versions.toml`; apply it with `alias(libs.plugins.walenje.<name>)`.

  | Kind | Plugin | Adds |
  |---|---|---|
  | Role (one per module) | `walenje.android-app` | Android app: SDK levels, Compose, JDK 25 |
  | | `walenje.library` | Android (namespace from the project path), iOS, JVM (dev only). No Compose |
  | | `walenje.feature` | library + compose + serialization + koin + logging + `core:ui`, `core:utils` |
  | Capability (opt-in) | `walenje.compose` | Compose Multiplatform, lifecycle, JVM UI tests, Android resources |
  | | `walenje.koin`, `walenje.serialization`, `walenje.logging` | Koin, kotlinx.serialization, Kermit |
  | Base | `walenje.multiplatform` | Kotlin Multiplatform, JDK 25, `-Xexpect-actual-classes`, test libraries |
  | | `walenje.formatting` | ktfmt (every module and the root) |

  Change a setting in the convention plugin, not in each module. Build-wide constants (JDK, root namespace) live in
  `WalenjeBuild` (`Project.kt`). A new plugin needs a class, a `gradlePlugin` entry and a catalog entry.
- iOS targets are opt-in: `-Pwalenje.ios=true` (CI and the Xcode build phase pass it). Without it Gradle skips
  Kotlin/Native entirely.
- Release targets are Android and iOS. The JVM target exists for `:app:desktop:hotRunJvm` and fast tests only. Don't
  add macOS, desktop packaging or other targets unless asked.
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
- **Navigation.** Navigation 3 (JetBrains `navigation3-ui`). `@Serializable` routes implementing `NavKey` and one
  `NavDisplay` with a saveable back stack live in `app/common/.../navigation/Navigation.kt`. Screens take navigation
  callbacks; they never receive the back stack.
- **UI.** Shared UI lives in common `src/` and goes through `core/designsystem` (theme, `Text`, `Button`, icons).
  Don't hard-code colours, fonts or sizes in feature code. `DESIGN.md` sets the visual direction (principles,
  tokens, components, screen recipes); check new screens and components against its review checklist.
- **Platform code.** Keep Android, iOS and JVM APIs in `src/androidMain/`, `src/iosMain/` and `src/jvmMain/` behind
  `expect`/`actual`. Every shared module targets Android, iOS and JVM, so a new `expect` needs an `actual` for each
  (the JVM one can be a dev stub). Never put `Context` or other Android types in common `src/`.
- **Logging.** Use Kermit through the module's `internal val logger`. Don't use `println`.

## Code style

- ktfmt, kotlinlang style, max width 100, unused imports removed. It covers module sources, all build scripts and
  `convention-plugins`. `ktfmtCheck` checks it and nothing reformats during a build, so run `./gradlew ktfmtFormat`
  before committing. `@Composable` functions are PascalCase. ktlint is not used: it disagrees with ktfmt's output.
- Four-space indentation, trailing commas allowed.

## Commands

Set `ANDROID_HOME` (for example `~/Library/Android/sdk`). iOS needs Xcode with its first-launch setup done
(`sudo xcodebuild -runFirstLaunch`). Build the tasks you need rather than `./gradlew build`, which also links
release binaries for every iOS target.

```sh
./gradlew :app:android:assembleDebug          # Android APK (installDebug to put it on a device or emulator)
./gradlew :app:desktop:hotRunJvm              # desktop dev app with Compose Hot Reload
./gradlew jvmTest testAndroidHostTest         # tests on JVM and Android (fast loop)
./gradlew :feature:onboarding:jvmTest         # one module's tests
./gradlew -Pwalenje.ios=true iosSimulatorArm64Test   # tests on the iOS simulator
./gradlew ktfmtCheck                          # formatting check (ktfmtFormat to fix; add -Pwalenje.ios=true
                                              # to cover iOS sources)
./cleanup.sh                                  # build output, Xcode user data, Kotlin/Native temp leftovers
./cleanup.sh --ide --deep                     # also .idea, the daemons and the project's .gradle
```

For iOS, open `app/ios/Walenje.xcodeproj` in Xcode and run the `Walenje` scheme on a simulator; its build phase
runs `./gradlew -Pwalenje.ios=true :app:common:embedAndSignAppleFrameworkForXcode`.

## Tests and CI

- Tests live in each module's `src/commonTest/kotlin`, so they run on JVM, Android (host tests), the iOS simulator,
  and use `kotlin.test`. Current coverage: `BaseViewModel` (`core/utils`), `SetPinViewModel`
  (`core/ui`), `SetPinScreenViewModel` (`feature/onboarding`) and the design tokens (`core/designsystem`).
- Compose UI tests (`runComposeUiTest` from `androidx.compose.ui.test.v2`; the v1 one is deprecated) live in
  `src/jvmTest/kotlin` and run on the JVM only: in `commonTest` they would also run as Android host tests, which
  need Robolectric. `walenje.compose` adds `compose-ui-test` to
  `jvmTest`. Pass the ViewModel to the screen instead of starting Koin. Example: `SetPinScreenTest`.
- ViewModels run on `viewModelScope`, so tests call `Dispatchers.setMain(UnconfinedTestDispatcher())` and use
  `runTest` from `kotlinx-coroutines-test` (every module's tests have it through `walenje.multiplatform`). Collect effects
  with `effect.take(n).toList()`.
- New business logic (ViewModels, use cases, repositories) gets tests in the same change.
- `.github/workflows/ci.yml` runs on `macos-26` for every push to `main` and every pull request: the Android APK,
  desktop dev JAR, iOS simulator framework, the tests on JVM, Android and iOS, and `ktfmtCheck`. It
  doesn't build the iOS app itself: Xcode can need a multi-GB iOS platform download on the runner. Keep it green.

## Git

- `main` is the default branch. Work on a feature branch.
- Conventional Commits: `type(scope): subject`, for example `build(deps): bump kotlin to 2.4.0`.
- Don't commit unrelated working-tree changes.
- To land a branch: `git fetch origin`, rebase the branch onto `origin/main`, fast-forward local `main` to
  `origin/main` and then to the branch, push `main` to `origin`, and delete the local branch. There are no pull
  requests.
