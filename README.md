# AI Personal Notification Assistant

A production-ready Android application that uses AI to help you understand and manage your notifications. Built with Jetpack Compose, Material Design 3, and Gemini AI.

## Features

- 🤖 **AI-Powered Chat** - Ask questions about your notifications using natural language
- 🔔 **Smart Notification Listening** - Automatically captures and filters important notifications
- 🔐 **Encrypted Local Storage** - All notifications stored securely on your device using SQLCipher
- 🎨 **Beautiful UI** - Material Design 3 with dark/light mode support
- ⚡ **Offline First** - Works completely offline except for AI responses
- 🔍 **Smart Filtering** - Automatically prioritizes important notifications
- 📊 **Notification History** - Access full history of stored notifications
- ⚙️ **Fully Configurable** - Adjust retention periods, API keys, and preferences

## Architecture

```
MVVM + Repository Pattern + Hilt DI + Coroutines/Flow
```

### Key Components

- **UI Layer** - Jetpack Compose screens with Material Design 3
- **ViewModel Layer** - Manages UI state and business logic
- **Repository Layer** - Abstracts data sources
- **Domain Layer** - Core business logic (ImportanceEngine, use cases)
- **Data Layer** - Room database + SQLCipher + DataStore preferences
- **Service Layer** - NotificationListenerService for capturing notifications
- **AI Layer** - Gemini API integration for natural language responses

### Database

- **Technology** - Room + SQLCipher (encrypted)
- **Storage** - Local device only, never uploaded
- **Retention** - Configurable (default 30 days for LOW priority)
- **Schema** - Notifications + Chat messages

## Requirements

- Android SDK: minSdk = 26, targetSdk = 35
- JDK 17+
- Gradle 8.x

## Setup

### 1. Clone Repository

```bash
git clone https://github.com/yourusername/AI-Notification-Assistant.git
cd AI-Notification-Assistant
```

### 2. Build without Android Studio

```bash
./gradlew assembleDebug      # Build debug APK
./gradlew assembleRelease    # Build release APK (unsigned)
```

### 3. Install Debug APK

```bash
./gradlew installDebug
```

### 4. Configure Gemini API Key

1. Open the app
2. Grant notification access permission when prompted
3. Tap Settings
4. Enter your Gemini API key from [Google AI Studio](https://aistudio.google.com/app/apikey)
5. Save and start using the app

## GitHub Actions CI/CD

### Automatic APK Building

Push to `main` or create a tag to trigger automated builds:

```bash
# Builds debug and release APKs
git push origin main

# Creates a release with APK artifacts
git tag v1.0.0
git push origin v1.0.0
```

The workflow will:
1. Build debug APK
2. Build release APK (unsigned)
3. Upload artifacts to GitHub Releases

### Download APKs

1. Go to GitHub Releases
2. Download `app-debug.apk` or `app-release-unsigned.apk`
3. Install using `adb install <apk>`

## Permissions

The app requires:
- **Notification Access** - To read notifications (system permission)
- **POST_NOTIFICATIONS** - To show notifications on Android 13+
- **Internet** - For Gemini API communication

## Privacy

✅ **All your data stays on your device**
- Notifications never uploaded to cloud
- Only notification data needed for current query sent to Gemini
- Encrypted local database
- No tracking or analytics
- Full user control over retention period

## Building from Source

### Prerequisites

```bash
# Verify Java 17
java -version

# Verify Gradle
gradle --version
```

### Build Commands

```bash
# Clean build
./gradlew clean

# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Run tests
./gradlew testDebugUnitTest

# Run Android tests
./gradlew connectedAndroidTest

# Generate lint report
./gradlew lint
```

## Project Structure

```
app/src/
├── main/
│   ├── kotlin/com/gaurav/ainotificationassistant/
│   │   ├── data/              # Data layer (DB, Repository)
│   │   ├── domain/            # Domain layer (Models, Use Cases)
│   │   ├── di/                # Hilt modules
│   │   ├── service/           # Background services
│   │   ├── ui/                # Compose UI
│   │   └── receiver/          # Broadcast receivers
│   └── res/                   # Resources
├── test/                      # Unit tests
└── androidTest/               # Instrumentation tests
```

## Configuration

### API Keys

Set Gemini API key in the app's Settings screen. The key is stored securely using DataStore Preferences encrypted file.

### Database Encryption

SQLCipher encryption is handled automatically:
- Keys stored in Android Keystore
- 256-bit AES encryption
- Initialized on first app launch

### Retention Policy

Configure in Settings:
- HIGH priority: Kept indefinitely
- MEDIUM priority: Default 30 days
- LOW priority: Auto-deleted after retention period

## Troubleshooting

### Notification Access Not Granted

1. Go to Settings > Apps > Special App Access > Notification Access
2. Find "AI Notification Assistant" and toggle ON
3. Return to the app

### Build Fails

```bash
# Clean and rebuild
./gradlew clean assembleDebug

# Update gradle wrapper
./gradlew wrapper --gradle-version=8.6

# Check Java version
java -version  # Should be 17 or higher
```

### Database Corruption

1. Go to Settings
2. Tap "Delete All Data"
3. Grant notification access again
4. Notifications will start fresh

## Development

### Adding Dependencies

Edit `gradle/libs.versions.toml` and add to `build.gradle.kts`:

```kotlin
implementation(libs.myNewDependency)
```

### Creating New Screens

1. Create ViewModel in `ui/screen/<name>/`
2. Create Composable screen function
3. Add to `navigation/AppNavGraph.kt`
4. Add to `Screen` sealed class

### Extending ImportanceEngine

Modify `domain/engine/ImportanceEngine.kt` to add:
- Custom package priorities
- Additional keyword matching
- Category-based rules

## Testing

```bash
# Unit tests
./gradlew testDebugUnitTest

# Android instrumentation tests
./gradlew connectedDebugAndroidTest

# Test report
./gradlew testDebugUnitTest --info
```

## Performance

- Supports 500,000+ notifications
- Instant full-text search
- Efficient SQLCipher queries
- Optimized Compose recomposition
- Background cleanup worker (weekly)

## Security

- ✅ End-to-end local encryption
- ✅ Android Keystore for key management
- ✅ ProGuard/R8 obfuscation enabled
- ✅ No cloud backup of data
- ✅ No analytics or tracking

## Contributing

Contributions welcome! Please:
1. Fork the repository
2. Create feature branch (`git checkout -b feature/amazing`)
3. Commit changes (`git commit -m 'Add feature'`)
4. Push to branch (`git push origin feature/amazing`)
5. Create Pull Request

## License

This project is open source and available under the MIT License.

## Support

For issues, questions, or suggestions:
1. Check GitHub Issues
2. Create a new issue with details
3. Include Android version and app version

## Acknowledgments

- Jetpack Compose for modern Android UI
- Material Design 3 for beautiful design system
- Firebase Generative AI for LLM capabilities
- Hilt for dependency injection
- Room for type-safe database access

---

**Privacy First** • **All Local** • **AI-Powered** • **Open Source**
