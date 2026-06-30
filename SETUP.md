# Setup Guide - AI Personal Notification Assistant

This guide will help you set up and build the Android application without Android Studio.

## Prerequisites

### Required Software

1. **Java Development Kit (JDK) 17+**
   ```bash
   java -version
   ```
   Should output: `openjdk version "17" or later`

   Download from: https://adoptium.net/

2. **Git**
   ```bash
   git --version
   ```

3. **Gradle** (Wrapper included, but optional locally)
   The project includes a gradle wrapper, so you don't need Gradle installed separately.

### Environment Variables

On Windows, add Java to PATH:
```
C:\Program Files\Java\jdk-17\bin
```

On Linux/Mac, add to `.bashrc` or `.zshrc`:
```bash
export JAVA_HOME=/path/to/jdk-17
export PATH=$JAVA_HOME/bin:$PATH
```

## Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/AI-Notification-Assistant.git
cd AI-Notification-Assistant
```

## Step 2: Download Gradle Wrapper JAR

The gradle wrapper JAR is needed. Download it:

```bash
mkdir -p gradle/wrapper
cd gradle/wrapper

# On Windows (use PowerShell):
Invoke-WebRequest -Uri "https://services.gradle.org/distributions/gradle-8.6-wrapper.jar" -OutFile "gradle-wrapper.jar"

# On Linux/Mac:
wget https://services.gradle.org/distributions/gradle-8.6-wrapper.jar
# or
curl -O https://services.gradle.org/distributions/gradle-8.6-wrapper.jar

cd ../..
```

Or manually download from: https://services.gradle.org/distributions/gradle-8.6-wrapper.jar

Place the JAR in: `gradle/wrapper/gradle-wrapper.jar`

## Step 3: Verify Setup

```bash
# Make gradlew executable (Linux/Mac only)
chmod +x gradlew

# Check if everything is set up
./gradlew --version
```

Should output Gradle 8.6 version info.

## Step 4: Build the Project

### Clean Build

```bash
./gradlew clean
```

### Build Debug APK

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

### Build Release APK (Unsigned)

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release-unsigned.apk`

## Step 5: Configure Firebase Gemini API

### Create Firebase Project

1. Go to https://console.firebase.google.com/
2. Click "Add Project"
3. Name it "AI Notification Assistant"
4. Complete setup steps
5. Enable Firestore (even though we won't use it; required for Firebase)

### Get Gemini API Key

1. Go to https://aistudio.google.com/app/apikey
2. Create or copy an API key
3. Keep it safe (you'll paste it in the app)

### Note

For this app, you can use the **Google AI API key directly** instead of Firebase. The app is configured to work with both:

- Go to https://aistudio.google.com/app/apikey
- Create an API key
- Use it in the app's Settings screen

No Firebase credentials file needed!

## Step 6: Install on Device

### Using adb

```bash
# Connect Android device via USB with USB debugging enabled

# Install debug APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Or reinstall (overwrite existing)
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

### Verify Installation

```bash
adb shell pm list packages | grep ainotification
```

Should output: `com.gaurav.ainotificationassistant`

## Step 7: Run the App

1. Open the app on your device
2. Grant **Notification Access** permission when prompted
3. Tap Settings gear icon
4. Enter your **Gemini API key**
5. Go back to Chat
6. Start asking about your notifications!

## Troubleshooting

### Build Issues

#### "gradlew not found"
```bash
# Make file executable (Linux/Mac)
chmod +x gradlew

# On Windows, use:
gradlew.bat assembleDebug
```

#### "JAVA_HOME not set"
```bash
# Set JAVA_HOME
export JAVA_HOME=/path/to/jdk-17  # Linux/Mac
set JAVA_HOME=C:\Program Files\Java\jdk-17  # Windows

# Or edit gradle.properties
org.gradle.java.home=/path/to/jdk-17
```

#### "gradle-wrapper.jar not found"
Download it manually as shown in Step 2.

#### Build timeout
```bash
# Increase timeout and memory
./gradlew assembleDebug --max-workers 4 -Dorg.gradle.jvmargs="-Xmx4g"
```

### Runtime Issues

#### "Notification Access Required" screen stuck
1. Go to Settings > Apps > Special app access > Notification access
2. Toggle **AI Notification Assistant** ON
3. Return to app

#### App crashes on startup
1. Delete app: `adb uninstall com.gaurav.ainotificationassistant`
2. Rebuild and reinstall: `./gradlew installDebug`

#### API key not working
1. Verify key is correct from https://aistudio.google.com/app/apikey
2. Check it's activated (has API calls enabled)
3. In app, go to Settings and re-enter it

### Device Issues

#### "device not found"
```bash
# Check connected devices
adb devices

# If not listed, check USB debugging is enabled:
# Settings > Developer options > USB debugging ON
```

#### Permission denied
```bash
# Linux/Mac - need sudo for some adb operations
sudo adb devices
```

## Build Variants

### Debug Build (Development)
- No obfuscation
- Debuggable
- Uses debug keystore

```bash
./gradlew assembleDebug
```

### Release Build (Production)
- Obfuscated (ProGuard/R8)
- Unsigned (sign manually)
- Optimized

```bash
./gradlew assembleRelease
```

## Advanced Options

### Clean Build Cache

```bash
./gradlew clean
rm -rf .gradle build app/build
```

### Build with Specific JDK

```bash
./gradlew assembleDebug --scan -DjavaHome=/path/to/jdk-17
```

### Enable Gradle Debugging

```bash
./gradlew assembleDebug --debug
```

### Check Dependencies

```bash
./gradlew dependencies
./gradlew dependencies --configuration debugRuntimeClasspath
```

## GitHub Actions (Automated Builds)

Push to GitHub to trigger automatic builds:

```bash
git remote add origin https://github.com/yourusername/AI-Notification-Assistant.git
git branch -M main
git push -u origin main
```

GitHub Actions will:
1. Build debug APK
2. Build release APK
3. Create artifacts in Actions tab
4. Attach to releases when you tag

## Next Steps

1. ✅ Build the app
2. ✅ Install on device
3. ✅ Grant permissions
4. ✅ Set Gemini API key
5. ✅ Start using!

## Getting Help

- Check GitHub Issues
- Review logs: `adb logcat | grep ainotification`
- Check build output for specific errors
- Verify Java version: `java -version`

## Resources

- Gradle Wrapper: https://docs.gradle.org/current/userguide/gradle_wrapper.html
- Android Build System: https://developer.android.com/build
- Gemini API: https://ai.google.dev/
- GitHub Actions: https://docs.github.com/en/actions

---

**Questions?** Open a GitHub issue with your error message and device info!
