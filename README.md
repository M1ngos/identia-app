# Identia

> **identIA** — a cross-platform identity-verification prototype built with Kotlin Multiplatform and Compose Multiplatform.

Identia is an **alpha prototype** of an identity-verification app that runs natively on **Android, iOS, and Desktop (JVM)** from a single shared Compose UI codebase. It walks a user through a PIN gate, document capture, selfie/liveness verification, and face authentication, plus a dashboard, an audit log, and settings — all driven by in-memory mock data.

The screens were rebuilt from a **Claude Design handoff** (HTML/CSS/JS mockups under `docs/identia-handoff/`); nothing in the app talks to a real backend.

## Status

This is an **alpha demo**, not a shippable product:

- **No backend / no real biometrics** — capture, liveness, and face matching are simulated UI flows with hardcoded sample values.
- **PIN gate is hardcoded** — enter the demo access code **`123456`** to get past the entry screen.
- Bottom-nav tabs: **Home · Verify · Face · Logs** (Settings is reached from Home).

## Tech stack

Kotlin Multiplatform · Compose Multiplatform · Material 3 · type-safe Navigation Compose · kotlinx.serialization. All UI lives in the shared `:shared` module; `:androidApp`, `:desktopApp`, and `iosApp/` are thin platform entry points. Versions are pinned in [`gradle/libs.versions.toml`](gradle/libs.versions.toml).

## Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- Desktop app:
  - Hot reload: `./gradlew :desktopApp:hotRun --auto`
  - Standard run: `./gradlew :desktopApp:run`
- iOS app: open the [`iosApp`](iosApp) directory in Xcode and run it from there.

## Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :shared:testAndroidHostTest`
- Desktop tests: `./gradlew :shared:jvmTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) and [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/).
