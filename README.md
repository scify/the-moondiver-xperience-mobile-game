
# The Moondiver Xperience - Mobile Game

<hr>

![Logo](https://play-lh.googleusercontent.com/Bs89RdnZXUHZpmyx3kdWCMDdmY8AgftTe_pCed0eB7W5WW4cxVNqg5qBO-KoT9lwFg=w720-h310)

<hr>

This project is about a mobile game for Android devices.
It was created using [LibGDX](https://github.com/libgdx/libgdx).




# The Moondiver Xperience - Mobile Game

This project is about a mobile game for Android devices.
It was created using [LibGDX](https://github.com/libgdx/libgdx).

## Project Setup

After cloning the project, follow these steps:

1. Copy the `config.properties` file from the Nextcloud folder to the `/android/assets/` directory.
2. Create a `local.properties` file at the root directory, with the information about Android SDK. Usually this file looks like this:

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
4. If you are using Intellij, in the left eplorer menu, left click the `android/src/org/scify/moonwalker/app/AndroidLauncher.java` file, and select "Run AndroidLauncher".

## Create the signed .apk file

Open the `/android` directory as an Android Studio project in Android Studio, and follow the trivial steps.