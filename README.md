# Walenje

[![CI](https://github.com/victorkambi/Walenje/actions/workflows/ci.yml/badge.svg)](https://github.com/victorkambi/Walenje/actions/workflows/ci.yml)

Walenje is a personal finance tracker built as a Kotlin Multiplatform app with Compose Multiplatform. The name is Swahili/Sheng for *wallet*, pronounced `[wah-leh-nje]`.

![Walenje icon](images/icon.svg)

> **Status:** early development. The onboarding flow (welcome, wallet explainer, PIN setup) and biometric authentication are in place. The Home, Analytics, and Profile screens are placeholders.

## Features

**Implemented**
- Onboarding: welcome screen, "secure your wallet" explainer, and PIN setup with confirmation
- Biometric authentication on Android and macOS (Touch ID); iOS and desktop (JVM) use placeholders for now
- Material 3 theming with light and dark color schemes and Montserrat typography

**Planned**
- Expense and income tracking
- Analytics
- Profile and settings

## Tech stack

- [Kotlin](https://kotlinlang.org/) 2.4 and [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) for shared logic across Android, iOS, macOS, and desktop
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform) for UI, with [Material 3](https://m3.material.io/components) components
- [Compose Navigation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation-routing.html) with type-safe, serializable routes
- [Koin](https://github.com/InsertKoinIO/koin) for dependency injection
- [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) and [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous work
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization) for route and data serialization
- [Kermit](https://github.com/touchlab/Kermit) for logging

## Architecture

The app follows MVI (Model-View-Intent) with a clean-architecture module split. Each screen has a contract (`Event`, `State`, `Effect`) and a ViewModel that extends `BaseViewModel`.

```
app/
  common/    shared app entry point, navigation, DI wiring (KMP)
  android/   Android application
  desktop/   desktop (JVM) application
  ios/       SwiftUI host for the shared Compose UI
  macos/     native macOS application (no JVM)
core/
  authentication/  biometric abstraction with per-platform actuals
  designsystem/    theme, typography, icons, shared components
  ui/              shared UI pieces such as the PIN keypad
  utils/           MVI base classes, UI state, logging
feature/
  onboarding/      welcome, secure-wallet, and PIN setup flow
  home/            home screen (placeholder)
  analytics/       analytics (not yet implemented)
  profile/         profile (not yet implemented)
build-logic/       Gradle convention plugins shared by the modules
```

Navigation is declared in `app/common/.../navigation/Navigation.kt`.

The project builds with Gradle. Shared module configuration lives in convention plugins under `build-logic/`, and versions live in `gradle/libs.versions.toml`.

## Requirements

- JDK 21 or newer to start Gradle. The Gradle daemon and the compilers run on JetBrains Runtime 25, which Gradle downloads if it isn't installed.
- Android SDK with compile SDK 37 (min SDK 24), with `ANDROID_HOME` pointing at it.
- A Mac with Xcode for the iOS and macOS targets. Run `sudo xcodebuild -runFirstLaunch` once after installing or updating Xcode.
- Optional: Android Studio or IntelliJ IDEA.

## Getting started

Clone the repository and run commands from its root. iOS and macOS targets are opt-in: pass `-Pwalenje.apple=true` to build or test them.

**Android**

```bash
./gradlew :app:android:installDebug
```

This builds the app and installs it on a connected device or emulator.

**Desktop**

```bash
./gradlew :app:desktop:run
```

Use `:app:desktop:hotRunJvm` instead to update the UI while the app runs (Compose Hot Reload), or `:app:desktop:packageDmg` for an installer.

**iOS**

Open `app/ios/Walenje.xcodeproj` in Xcode and run the `Walenje` scheme on a simulator. The Xcode build runs Gradle to build the shared framework.

**macOS (native)**

```bash
scripts/macos-app.sh --open
```

This builds the Kotlin/Native app and packages it with its resources as `app/macos/build/macos/Walenje.app`. Unlock uses Touch ID.

**Tests and formatting**

```bash
./gradlew jvmTest testAndroidHostTest
./gradlew -Pwalenje.apple=true iosSimulatorArm64Test macosArm64Test
./gradlew ktfmtFormat
```

**Cleaning build output**

```bash
./cleanup.sh
```

This removes build output, Xcode user data and Kotlin/Native temp files. Add `--ide` to also remove `.idea`, and `--deep` to stop the Gradle daemons and remove the project's `.gradle` directory.

## Screenshots

### Android

![Screenshot showing the user journey](images/journey.png "Screenshot showing the user journey")

### iOS

_Coming soon._

### Desktop

_Coming soon._

## Design

Designed by Kambi.

## Contributing

This is a personal project. Issues and suggestions are welcome, but pull requests are not being accepted yet.

## License

```
Copyright 2024 Kambi Victor

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
