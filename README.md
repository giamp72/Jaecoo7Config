# Jaecoo7Config

Jaecoo7Config is a simple Android starter project for the Jaecoo 7 infotainment system.

The goal of this app is to provide a clean, local profile manager that can persist vehicle-related preferences on the device and restore them later. This is a safe, device-local implementation for Android head units that do not expose the vehicle settings API directly to third-party apps.

## Current scope

- Android app skeleton built with Kotlin and Jetpack Compose
- Persisted settings using DataStore
- Simple UI to edit and save a preferred configuration
- Profile restoration from saved preferences
- Design suited for future integration with OEM-provided vehicle APIs or head-unit-specific controls

## Important note

This project does not automatically control the vehicle hardware by itself. In many infotainment systems, direct access to real vehicle settings requires OEM permissions, system-level privileges, or manufacturer APIs. This app is therefore designed as a practical foundation for saving and restoring configuration data locally and for future integration work on supported hardware.

## Run locally

Open the project in Android Studio and sync Gradle.

1. Install Android Studio
2. Open the repo as a project
3. Let Gradle sync
4. Run the app on an Android emulator or a compatible device

## Project structure

- `app/` — Android application module
- `settings.gradle.kts` — project settings
- `build.gradle.kts` — root Gradle configuration

## License

MIT
