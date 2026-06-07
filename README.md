# USB Master - Offline Storage Utility

A fully functional Android application for managing external USB storage devices entirely offline.

## Features
- **Fill Storage**: Fills remaining space with FF (all ones) or 00 (all zeros) patterns
- **Wipe Storage**: Recursively deletes all files and directories, including hidden content
- **Format Utility**: Provides guidance for formatting drives to FAT32 or exFAT
- **100% Offline**: Requires no internet permissions or connectivity
- **No Crypto Issues**: Uses simple pattern-based filling without memory constraints

## How to Build with Android Studio

1. **Extract the ZIP file** to your computer
2. **Open Android Studio**
3. Click **"Open an Existing Project"**
4. Navigate to the extracted `usbmaster` folder and select it
5. Wait for Gradle to sync (it will download dependencies automatically)
6. Click **Build** → **Build Bundle(s)/APK(s)** → **Build APK(s)**
7. The APK will be generated in: `app/build/outputs/apk/debug/app-debug.apk`

## How to Build from Command Line

### On Windows:
```bash
cd usbmaster
gradlew.bat build
```

### On Mac/Linux:
```bash
cd usbmaster
chmod +x gradlew
./gradlew build
```

The APK will be located at: `app/build/outputs/apk/debug/app-debug.apk`

## Project Structure
```
usbmaster/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/usbmaster/
│   │       │   ├── MainActivity.java
│   │       │   └── UsbStorageManager.java
│   │       ├── res/
│   │       │   └── layout/
│   │       │       └── activity_main.xml
│   │       └── AndroidManifest.xml
│   └── build.gradle
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew (Unix/Mac/Linux)
├── gradlew.bat (Windows)
├── build.gradle
└── settings.gradle
```

## Permissions
The app requests `MANAGE_EXTERNAL_STORAGE` to ensure it can access all files, including hidden ones, for complete wipe and fill operations.

## Important Notes
- **Fill FF Pattern**: Writes FFFFFFFF... to all available space
- **Fill 00 Pattern**: Writes 00000000... to all available space
- **Wipe Operation**: Permanently deletes all files - cannot be undone
- **Format Operation**: Must be completed through your device's system settings

## Troubleshooting

### Gradle Wrapper Issues
If you get errors about `gradlew` not being found:
- Make sure you extracted the entire ZIP file
- On Mac/Linux, run: `chmod +x gradlew`
- The wrapper files are included in the `gradle/wrapper/` directory

### Build Errors
- Ensure you have Java 11 or higher installed
- Make sure Android SDK is properly configured in Android Studio
- Try: **File** → **Invalidate Caches** → **Restart**

## Version
- Version: 1.0
- Minimum SDK: 24 (Android 7.0)
- Target SDK: 33 (Android 13)
