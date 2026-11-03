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
  android/   Android application shell
  ios/       SwiftUI host for the Compose iOS framework (Xcode project)
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
```

Navigation is declared in `app/common/.../navigation/Navigation.kt`. Modules are wired with the Gradle convention plugins in `convention-plugins/`.

## Requirements

- JDK 21 or newer (Gradle provisions JDK 25 for its daemon through the foojay toolchain resolver)
- Android Studio (for Android) and Xcode (for iOS), with the Kotlin Multiplatform plugin if you use Android Studio
- Android SDK with compile SDK 37 (min SDK 24)
- A Mac with Xcode for the iOS target

## Getting started

Clone the repository and open the project root in Android Studio or IntelliJ IDEA. Gradle syncs on open.

**Android**

```bash
./gradlew :app:android:assembleDebug
```

Or run the `app:android` configuration from Android Studio.

**Desktop**

```bash
./gradlew :app:common:run
```

**iOS**

Open `app/ios/Walenje.xcodeproj` in Xcode and run the `Walenje` scheme on a simulator or device. The Xcode build phase builds the shared `common` framework through Gradle.

**Cleaning build output**

```bash
./cleanup.sh
```

This removes Gradle, Xcode, and IDE artifacts. It is a local helper and is not required for normal builds.

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
