# AGENTS.md

Guidance for coding agents working in this repository. Read `README.md` for the product overview.

## What this is

Walenje is a personal finance tracker: a Kotlin Multiplatform app with a Compose Multiplatform UI that targets
Android, iOS and desktop (JVM). It is in early development. Onboarding (welcome, secure-wallet explainer, PIN setup)
and biometrics work; Home, Analytics and Profile are placeholders.

## Modules

| Path | Role |
|---|---|
| `app/common` | Shared app entry (`App.kt`), navigation graph, Koin wiring, desktop `main` and iOS `MainViewController` |
| `app/android` | Android application shell (`MainActivity`, `MainApplication`) |
| `app/ios` | SwiftUI host for the `common` framework (Xcode project) |
| `core/authentication` | `Biometrics` expect/actual per platform |
| `core/designsystem` | Material 3 theme, Montserrat typography, icons, shared components |
| `core/ui` | Shared UI pieces such as the PIN keypad and its ViewModel |
| `core/utils` | MVI base classes (`mvi/`), UI state types |
| `feature/onboarding` | Welcome, secure-wallet and PIN setup screens (package `feature.welcome`) |
| `feature/home`, `feature/analytics`, `feature/profile` | Placeholders |

Packages live under `kambi.victor.walenje`. Shared build configuration is in the Gradle convention plugins under
`convention-plugins/conventions/src/main/kotlin/` (`walenje-kmp`, `walenje-cmp-base`, `walenje-kmp-library`,
`walenje-application-feature`, `walenje-koin`, `walenje-kotlinx`, `walenje-logger`, `walenje-format-code`).
Versions live only in `gradle/libs.versions.toml`.

## Architecture rules

- **MVI.** Each screen has a contract interface with `Event : UiEvent`, `State : UiState` and `Effect : UiEffect`
  (see `feature/onboarding/.../SetPinContract.kt`). Its ViewModel extends
  `core/utils/.../mvi/BaseViewModel` and changes state only through `setState`, `setEvent` and `setEffect`.
- **DI.** Use Koin. Each feature exposes a `module { }` (for example `welcomeModules`) and registers ViewModels with
  `viewModelOf`. The app-level configuration is `createKoinConfiguration()` in `app/common/.../di/`.
- **Navigation.** Type-safe `@Serializable` routes in `app/common/.../navigation/Navigation.kt`. Screens take
  navigation callbacks; they never receive a `NavController`.
- **UI.** Shared UI lives in `commonMain` and goes through `core/designsystem` (theme, `Text`, `Button`, icons).
  Don't hard-code colours, fonts or sizes in feature code.
- **Platform code.** Keep Android, iOS and JVM APIs in `androidMain`, `iosMain` and `jvmMain` behind `expect`/`actual`.
  Never put `Context` or other Android types in `commonMain`.
- **Logging.** Use Kermit through the module's `internal val logger`. Don't use `println`.

## Code style

- ktfmt, Google style, max width 100, unused imports removed (`walenje-format-code`). `./gradlew build` reformats,
  so commit the formatted result.
- ktlint (`android_studio` style) with Slack's compose-lints rules, configured in `.editorconfig`. `@Composable`
  functions are PascalCase.
- Two-space indentation, trailing commas allowed.

## Commands

The JDK comes from Gradle's daemon toolchain (`gradle/gradle-daemon-jvm.properties`). The convention build needs
JDK 21 or newer.

```sh
./gradlew build                                   # compile all targets and format
./gradlew ktlintCheck                             # lint
./gradlew :app:android:assembleDebug              # Android APK
./gradlew :app:common:run                         # desktop app
./gradlew :app:common:linkDebugFrameworkIosSimulatorArm64   # iOS framework; run app/ios/Walenje.xcodeproj in Xcode
./cleanup.sh                                      # remove build, Xcode and IDE output
```

There are no tests yet. When you add them, put them in `commonTest` with `kotlin.test`.

## Git

- `main` is the default branch. Work on a feature branch.
- Conventional Commits: `type(scope): subject`, for example `build(deps): bump kotlin to 2.4.0`.
- Don't commit unrelated working-tree changes.
- To land a branch: rebase it onto `main`, fast-forward merge it into `main` locally, push `main` to `origin`,
  then delete the local branch. There are no pull requests.
