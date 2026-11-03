# Walenje

Walenje is a personal finance tracker built as a Kotlin Multiplatform app with Compose Multiplatform. The name is Swahili/Sheng for *wallet*, pronounced `[wah-leh-nje]`.

![Walenje icon](images/icon.svg)

> **Status:** early development. The onboarding flow (welcome, wallet explainer, PIN setup) and biometric authentication are in place. The Home, Analytics, and Profile screens are placeholders.

## Features

**Implemented**
- Onboarding: welcome screen, "secure your wallet" explainer, and PIN setup with confirmation
- Biometric authentication on Android, iOS, and desktop (JVM)
- Material 3 theming with light and dark color schemes and Montserrat typography

**Planned**
- Expense and income tracking
- Analytics
- Profile and settings

## Tech stack

- [Kotlin](https://kotlinlang.org/) 2.4 and [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) for shared logic across Android, iOS, and desktop
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
plugins/           ktfmt plugin for the build
templates/         shared module configuration
```

Navigation is declared in `app/common/.../navigation/Navigation.kt`.

The project builds with [Kotlin Toolchain](https://kotlin-toolchain.org/) rather than Gradle. Each module has a `module.yaml`, shared settings live in `templates/`, and versions live in `libs.versions.toml`.

## Requirements

- macOS, Linux or Windows. The `./kotlin` wrapper downloads the Kotlin Toolchain and its own JDK on first run.
- Android SDK with compile SDK 37 (min SDK 24). Point `ANDROID_HOME` at an existing SDK, or let the toolchain provision one.
- A Mac with Xcode for the iOS target. Run `sudo xcodebuild -runFirstLaunch` once after installing or updating Xcode.
- Optional: IntelliJ IDEA or Android Studio with the Kotlin Toolchain plugin.

## Getting started

Clone the repository and run commands from its root.

**Build everything**

```bash
./kotlin build
```

**Android**

```bash
./kotlin run -m android
```

This installs and starts the app on a connected device or emulator.

**Desktop**

```bash
./kotlin run -m desktop
```

Add `--compose-hot-reload` to update the UI while the app runs.

**iOS**

```bash
./kotlin run -m ios
```

Or open `app/ios/module.xcodeproj` in Xcode and run the `app` scheme.

**Formatting and checks**

```bash
./kotlin do ktfmtFormat
./kotlin check
```

**Cleaning build output**

```bash
./cleanup.sh
```

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
