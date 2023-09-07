# The Moondiver Xperience - Mobile Game

<hr>

![Logo](https://play-lh.googleusercontent.com/Bs89RdnZXUHZpmyx3kdWCMDdmY8AgftTe_pCed0eB7W5WW4cxVNqg5qBO-KoT9lwFg=w720-h310)

<hr>

This project is about a mobile game for Android devices. It was created
using [LibGDX](https://github.com/libgdx/libgdx).

# The Moondiver Xperience - Mobile Game

This project is about a mobile game for Android devices. It was created
using [LibGDX](https://github.com/libgdx/libgdx).

## Project Setup

After cloning the project, follow these steps:

1. Copy the `config.properties` file from the Nextcloud folder to the `/android/assets/` directory.
2. Create a `local.properties` file at the root directory, with the information about Android SDK.
   Usually this file looks like this:

    ```bash
        ## This file must *NOT* be checked into Version Control Systems,
        # as it contains information specific to your local configuration.
        #
        # Location of the SDK. This is only used by Gradle.
        # For customization when using a Version Control System, please read the
        # header note.
        #Fri Sep 16 11:29:31 EEST 2022
        sdk.dir=/home/paul/Android/Sdk
    ```
3. Copy the `google-services.json` file from the Nextcloud folder to the `/android` directory.
4. If you are using Intellij, in the left explorer menu, left-click
   the `android/src/org/scify/moonwalker/app/AndroidLauncher.java` file, and select "Run
   AndroidLauncher".

## Build & sign for Android

Run:

```bash
./gradlew clean

./gradlew build
```

This will create both an unsigned debug APK file in the `android/build/outputs/apk/debug` directory, 
and a release (unsigned though) .apk file in the `android/build/outputs/apk/release` directory. Before
you can install or publish this APK, you
must [sign it](https://developer.android.com/studio/publish/app-signing). The APK build by the above
command is already in release mode, you only need to follow the steps for keytool and jarsigner. You
can install this APK file on any Android device that
allows [installation from unknown sources](https://developer.android.com/distribute/marketing-tools/alternative-distribution#unknown-sources)
.

### Signing using Android Studio

The easiest and most convenient way is to produce the Android Bundle (aab) file, using Android Studio.
Select Build -> Generate Signed Bundle / APK 

### Manual Signing
In order to sign the unsigned .apk, run:

```bash
cd android/build/outputs/apk/release

jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore SciFY.keystore android-release-unsigned.apk SciFY

/home/paul/Android/Sdk/build-tools/32.0.0/zipalign -v 4 android-debug.apk android-release-signed.apk

/home/paul/Android/Sdk/build-tools/32.0.0/apksigner sign --ks SciFY.keystore --v1-signing-enabled true --v2-signing-enabled true android-release-signed.apk
```

Then, upload the `android-release-signed.apk` to the Google Play Developer Console.

Also, make sure to upload the following files:

```bash
android/build/outputs/mapping/release/mapping.txt

android/build/intermediates/merged_native_libs/debug/out/lib/natives.zip # the natives.zip should be created by you and include all directories except the "armeabi" one.
```
