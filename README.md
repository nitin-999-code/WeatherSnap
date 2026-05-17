# WeatherSnap

WeatherSnap is a complete Android application built with Jetpack Compose that allows users to search live weather for a city, capture a photo using a custom CameraX implementation, compress the image locally, add notes, and save the weather report locally using Room Database.

## Tech Stack
- **Kotlin**
- **Jetpack Compose** for modern UI
- **MVVM Architecture** with StateFlow and Coroutines
- **Dagger Hilt** for Dependency Injection
- **Navigation Compose** for screen routing
- **Retrofit & Gson** for networking
- **OkHttp** for logging interceptor
- **Room Database** for local persistence
- **CameraX** for custom camera preview and capture
- **Coil** for image loading
- **Material 3** styling

## Setup & Run Steps
1. Open the project in Android Studio.
2. Build and run the app on an emulator or physical device (physical device recommended for Camera functionality).
3. No API keys are required as it uses the open `Open-Meteo` API.

## APIs Used
1. **Geocoding API**: `https://geocoding-api.open-meteo.com/v1/search`
2. **Forecast API**: `https://api.open-meteo.com/v1/forecast`

## Features Implemented
- **City Search**: Autocomplete for city search. Fetches data when query length > 2.
- **Live Weather**: Fetches current temperature, humidity, wind speed, pressure, and condition.
- **Custom Camera**: Takes a photo using CameraX natively without delegating to an external camera intent.
- **Image Compression**: Scales down and compresses large images to save space.
- **Create Report**: Bundles weather snapshot, captured photo, and custom notes.
- **Local Storage**: Saves reports locally in Room Database, allowing them to be viewed even after app restarts.

## Protection against lifecycle / data-loss
The Create Report screen uses `SavedStateHandle` injected into the `CreateReportViewModel`. When the user navigates to the screen, selected weather, captured image paths, and entered notes are pushed into `SavedStateHandle`.
- **Rotation**: `SavedStateHandle` survives configuration changes, maintaining the exact draft report context.
- **Backgrounding**: The OS might kill the app to reclaim memory, but `SavedStateHandle` is serialized and restored, meaning if the app is resumed, the draft states are not lost.
- **Prevention of Duplicate Saves**: A simple `isSaving` flag ensures double-tapping doesn't trigger multiple inserts.
- **Temporary File Cleanup**: After a successful save, the final image is copied from the app cache to `filesDir` to persist securely. Original and temporary compressed files are deleted to save disk space.

## Image Compression
Handled by a custom `ImageCompressor` utility.
- It uses `BitmapFactory.Options` with `inJustDecodeBounds` to read the image size without loading it into memory.
- It calculates `inSampleSize` to scale down the image so its maximum dimension is at most ~1280px.
- It compresses the bitmap to a JPEG file in the cache directory with 75% quality.
- This effectively shrinks large camera images from several MBs down to a few hundred KBs or less.

## Known Tradeoffs
- Navigating with `WeatherSnapshot` is done by serializing it to JSON. This is straightforward and robust for Compose Navigation, but URL encoding JSON strings can look bloated. However, it completely avoids issues of passing complex Parcelables directly as arguments which is deprecated or restricted.
- The `CameraViewModel` is currently empty because camera interactions inside Compose using CameraX's `ProcessCameraProvider` naturally bind tightly to the `Context` and `LifecycleOwner` at the UI layer.

## Screen Recording Checklist
- [ ] 1. City autocomplete
- [ ] 2. Weather search
- [ ] 3. Create report
- [ ] 4. Custom camera screen
- [ ] 5. Image compression sizes
- [ ] 6. Notes entry
- [ ] 7. Save report
- [ ] 8. Saved reports list
