# AGENTS.md

Guidance for coding agents working in this repository. Read `README.md` for the product overview.

## What this is

Walenje is a personal finance tracker: a Kotlin Multiplatform app with a Compose Multiplatform UI that targets
Android, iOS and desktop (JVM). It is in early development. Onboarding (welcome, secure-wallet explainer, PIN setup)
and biometrics work; Home, Analytics and Profile are placeholders.

## Modules

| Path | Role |
|---|---|
| `app/common` | Shared app entry (`App.kt`), navigation graph, Koin wiring and iOS `MainViewController` |
| `app/android` | Android application (`MainActivity`, `MainApplication`, manifest, launcher resources) |
| `app/desktop` | Desktop application (`main.kt`) |
| `app/ios` | SwiftUI host; Swift imports the shared code from the `KotlinModules` framework |
| `core/authentication` | `Biometrics` expect/actual per platform |
| `core/designsystem` | Material 3 theme, Montserrat typography, icons, shared components |
| `core/ui` | Shared UI pieces such as the PIN keypad and its ViewModel |
| `core/utils` | MVI base classes (`mvi/`), UI state types |
| `feature/onboarding` | Welcome, secure-wallet and PIN setup screens (package `feature.welcome`) |
| `feature/home`, `feature/analytics`, `feature/profile` | Placeholders |

Packages live under `kambi.victor.walenje`.

## Build

The build uses [Kotlin Toolchain](https://kotlin-toolchain.org/) (the `./kotlin` wrapper, pinned to 0.13.0), not
Gradle. Use the `kotlin-tooling-kotlin-toolchain` skill for YAML syntax.

- `project.yaml` lists the modules and the local plugins. Each module has a `module.yaml`.
- Shared configuration lives in `templates/*.module-template.yaml`, applied with `apply:`. `base` (Kotlin 2.4.21,
  JVM 21, Compose 1.12.2, formatters) goes on every module; `android`, `cmp`, `kotlinx`, `koin`, `logger` and
  `feature` go where needed. Change a setting in the template, not in each module.
- Versions live only in `libs.versions.toml`. Reference them as `$libs.<key>`; never write Maven
  coordinates into a `module.yaml`.
- Sources: `src/` is common code, `src@android/`, `src@ios/` and `src@jvm/` are platform code, `test/` is common
  tests, `composeResources/` holds Compose resources.
- `plugins/ktfmt` is a vendored local plugin (see the source note in its `module.yaml`).
- `MIGRATION_PLAN.md` records the move from Gradle and what was dropped.

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
- **Platform code.** Keep Android, iOS and JVM APIs in `src@android/`, `src@ios/` and `src@jvm/` behind
  `expect`/`actual`. Never put `Context` or other Android types in common `src/`.
- **Logging.** Use Kermit through the module's `internal val logger`. Don't use `println`.

## Code style

- ktfmt, Google style, max width 100, unused imports removed. It runs as a check in `./kotlin check` and never
  reformats during a build, so run `./kotlin do ktfmtFormat` before committing. `@Composable` functions are
  PascalCase. ktlint is not used: it disagrees with ktfmt's output.
- Two-space indentation, trailing commas allowed.

## Commands

The wrapper downloads the CLI and provisions its own JDK. Set `ANDROID_HOME` (for example
`~/Library/Android/sdk`) to reuse an installed Android SDK. iOS needs Xcode with its first-launch setup done
(`sudo xcodebuild -runFirstLaunch`).

```sh
./kotlin build                       # compile and link everything
./kotlin build -p jvm -p android     # skip the iOS targets
./kotlin check                       # tests plus the ktfmt check
./kotlin do ktfmtFormat              # format sources (add -m <module> for one module)
./kotlin run -m desktop              # desktop app (add --compose-hot-reload for Hot Reload)
./kotlin run -m android              # install and start on a device or emulator
./kotlin run -m ios                  # iOS simulator; or open app/ios/module.xcodeproj in Xcode
./kotlin show dependencies -m <module>
./cleanup.sh                         # remove build and IDE output
```

Module names in `-m` are directory names (`common`, `designsystem`, `onboarding`, ...). There are no tests yet.
When you add them, put them in the module's `test/` directory with `kotlin.test`.

## Git

- `main` is the default branch. Work on a feature branch.
- Conventional Commits: `type(scope): subject`, for example `build(deps): bump kotlin to 2.4.0`.
- Don't commit unrelated working-tree changes.
- To land a branch: rebase it onto `main`, fast-forward merge it into `main` locally, push `main` to `origin`,
  then delete the local branch. There are no pull requests.
