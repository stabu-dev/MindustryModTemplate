# MindustryModTemplate

An advanced Java mod template for [Mindustry](https://github.com/Anuken/Mindustry), designed for experienced modders. It features a robust annotation-driven code generation system, an automated asset processing pipeline, GitHub Actions CI for cross-platform builds, and Jabel integration for modern Java syntax with Java 8 bytecode compatibility.

This template is based on the public version of the [ProjectUnity](https://github.com/AvantTeam/ProjectUnityPublic) mod by the AvantTeam.

## Using This Template

Before diving in, a good understanding of Java and Git is **highly recommended**. While not impossible to start without, you'll likely encounter fewer hurdles with prior experience. The Mindustry modding community, particularly on [Discord](https://discord.gg/mindustry), is a great resource for help.

1.  **Install Prerequisites:**
    *   **JDK 17 or higher:** This is essential for compiling this.
    *   **IDE (Recommended):** [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Community Edition is free) is strongly suggested over basic text editors.
2.  **Create Your Repository:**
    *   Click the `Use this template` button on the [GitHub repository page](`https://github.com/stabu-dev/MindustryModTemplate`).
    *   Select "Create a new repository".
3.  **Clone Your Repository:**
    *   Clone the newly created repository to your local machine.

> [!IMPORTANT]
> A **local copy** is *not* the ZIP archive you can download from GitHub. Use `git clone https://github.com/YOUR_USERNAME/YOUR_REPOSITORY_NAME` or the cloning feature provided by your Git client (like GitHub Desktop). 
> 
> Downloading the ZIP bypasses Git's version control capabilities.

4.  **Configure Your Mod:**
    This template is designed to make initial setup straightforward. You'll primarily need to modify the following:

    *   **`mod.json`:** This file contains your mod's metadata.
        *   `name`: The internal, unique ID for your mod (e.g., `my-awesome-mod`). This is critical and used by Gradle, generators, annotations, tools, and Mindustry itself. **It's highly recommended that your main Java package and related initial `template` folders (like `main/src/template`, `annotations/src/template`, `tools/src/template`) are renamed to match this value (e.g., if `name` is `my-awesome-mod`, rename the `template` package to `myawesomemod`). This consistency greatly improves clarity, convenience, and compatibility with the template's systems.**
        *   `displayName`: The user-friendly name shown in Mindustry's mod browser (e.g., `My Awesome Mod`). This also influences the default names of your built JAR files.
        *   `author`: Your name or your team's name.
        *   `description`: A brief description of your mod.
        *   `version`: Your mod's version (e.g., `1.0.0`).
        *   `minGameVersion`: The minimum Mindustry version your mod supports (e.g., `146`).
        *   `main`: The fully qualified name of your main mod class (e.g., `myawesomemod.MyAwesomeMod`).
    *   **`gradle.properties`:**
        *   `classPrefix`: A prefix for certain generated Java classes (like `YourPrefixSounds.java`, `YourPrefixEntityMapping`). If left empty, it defaults to your `displayName` from `mod.json` (with spaces removed). For example, if `displayName` is "My Awesome Mod", `classPrefix` would default to `MyAwesomeMod`.
        *   `mindustryPath` (Optional): If you want the `./gradlew install` task to copy the built mod to a custom Mindustry mods directory, specify the path here. Leave empty for automatic detection.
    *   **Java Source Files:**
        *   Rename the default package `template` in `main/src/` to a name that preferably matches your `mod.json` `name` (e.g., if `mod.json` `name` is `my-awesome-mod`, rename the `template` package to `myawesomemod`).
        *   Rename the main mod class `main/src/(your-new-package-name)/Template.java` to your desired class name (e.g., `MyAwesomeMod.java`).
        *   Ensure the `main` property in `mod.json` matches the new fully qualified name of this class (e.g., `myawesomemod.MyAwesomeMod`).
        *   You also need to refactor package names within the `annotations` and `tools` modules (e.g., from `template.annotations.processors` to `myawesomemod.annotations.processors`, and similar for `tools/src/template`).
    *   **GitHub Actions CI (`.github/workflows/ci.yml`):**
        *   The artifact names and release file names are *dynamically generated* based on the `displayName` in your `mod.json`. No manual changes for naming are typically needed in `ci.yml` after the initial setup of `mod.json`.

    Here's an example of a properly configured mod base, assuming the mod is named "My Awesome Mod" (with internal name `my-awesome-mod`):

    ```mermaid
    ---
    title: Project Hierarchy (Initial State & Example for "My Awesome Mod")
    ---
    graph LR;
    %%{init:{'flowchart':{'nodeSpacing': 10, 'rankSpacing': 10}}}%%;

    classDef folder fill:#465768,stroke:#bdcedf;
    classDef file fill:#468868,stroke:#bdffdf;
    classDef importantFile fill:#884668,stroke:#ffbddf,font-weight:bold;

    root{{"MindustryModTemplate (root)"}};
    github{{".github/"}};
    workflows{{"workflows/"}};
    annotations_mod{{"annotations/"}};
    annotations_src{{"src/"}};
    annotations_src_template_pkg{{"template/ (initial)"}};
    annotations_src_myawesome_pkg{{"myawesomemod/ (renamed)"}};
    main_mod{{"main/"}};
    main_assets{{"assets/"}};
    main_assets_raw{{"assets-raw/"}};
    main_src{{"src/"}};
    main_src_template_pkg{{"template/ (initial)"}};
    main_src_myawesome_pkg{{"myawesomemod/ (renamed)"}};
    tools_mod{{"tools/"}};
    tools_src{{"src/"}};
    tools_src_template_pkg{{"template/ (initial)"}};
    tools_src_myawesome_pkg{{"myawesomemod/ (renamed)"}};

    class root,github,workflows,annotations_mod,annotations_src,annotations_src_template_pkg,annotations_src_myawesome_pkg,main_mod,main_assets,main_assets_raw,main_src,main_src_template_pkg,main_src_myawesome_pkg,tools_mod,tools_src,tools_src_template_pkg,tools_src_myawesome_pkg folder;

    ci_yml(["ci.yml"]);
    mod_json(["mod.json"]);
    gradle_properties(["gradle.properties"]);
    root_build_gradle(["build.gradle"]);
    settings_gradle(["settings.gradle"]);
    main_build_gradle(["build.gradle"]);
    template_java(["Template.java (initial)"]);
    my_awesome_mod_java(["MyAwesomeMod.java (renamed)"]);

    class ci_yml, root_build_gradle, settings_gradle, main_build_gradle file;
    class mod_json, gradle_properties, template_java, my_awesome_mod_java importantFile;

    root-->github-->workflows-->ci_yml;
    root-->annotations_mod-->annotations_src;
        annotations_src --> annotations_src_template_pkg;
        subgraph After Renaming Annotations
            annotations_src --> annotations_src_myawesome_pkg;
        end
    root-->main_mod;
        main_mod-->main_assets;
        main_mod-->main_assets_raw;
        main_mod-->main_src;
            main_src --> main_src_template_pkg --> template_java;
            subgraph After Renaming Main
                main_src --> main_src_myawesome_pkg --> my_awesome_mod_java;
            end
        main_mod-->main_build_gradle;
    root-->tools_mod-->tools_src;
        tools_src --> tools_src_template_pkg;
        subgraph After Renaming Tools
            tools_src --> tools_src_myawesome_pkg;
        end
    root-->mod_json & gradle_properties & root_build_gradle & settings_gradle;
    ```

    Example changes:

    `mod.json`:
    ```diff
    {
    -    "name": "template",
    -    "displayName": "Template",
    -    "author": "Someone",
    -    "description": "No description provided",
    +    "name": "my-awesome-mod",
    +    "displayName": "My Awesome Mod",
    +    "author": "A Modder",
    +    "description": "An awesome mod for Mindustry!",
        "version": "1.0",
        "minGameVersion": 149,
    -    "main": "template.Template",
    +    "main": "myawesomemod.MyAwesomeMod",
        "java": true,
        "hideBrowser": true
    }
    ```

    `gradle.properties` (only `classPrefix` shown, others might be adjusted):
    ```diff
    # ...
    - classPrefix =
    + classPrefix = MyAwesomeMod
    # ...
    ```

    `main/src/myawesomemod/MyAwesomeMod.java` (after renaming package `template` to `myawesomemod` and file `Template.java` to `MyAwesomeMod.java`):
    ```diff
    - package template;
    + package myawesomemod;

    import arc.*;
    import arc.util.*;
    // ... other imports ...
    - import template.gen.*;
    + import myawesomemod.gen.*; // Assuming classPrefix led to this, or it's your gen package

    - public class Template extends Mod{
    + public class MyAwesomeMod extends Mod{
        // ...
    }
    ```

5.  **Asset Workflow:**
    *   Place your **raw, unprocessed assets** (e.g., original PNGs for sprites, WAV files for sounds) into the `main/assets-raw/` directory. Use a logical subdirectory structure (e.g., `sprites/units/`, `sounds/effects/`).
    *   Run the asset processing task: `./gradlew tools:proc`.
    *   Processed assets will be output to `main/assets/`, mirroring the structure from `assets-raw/`. These are the assets bundled into your mod.
    *   Generated asset classes like `Regions.java`, `MyAwesomeModSounds.java` (if `classPrefix` is `MyAwesomeMod`) will be created/updated in the `main/build/generated/myawesomemod/gen/` directory (assuming your main package is `myawesomemod`) and automatically included in compilation.

    That's the core setup! You can now start developing your mod.

## Building the Mod

Mindustry Java mods are typically cross-platform. Builds are managed via Gradle.

### Desktop Build (PC)

Ideal for quick testing on PC. The resulting JAR will have `Desktop` appended (e.g., `MyAwesomeModDesktop.jar`).

1.  Open your terminal in the project's root directory.
2.  Ensure you have an internet connection for the first build or after a `./gradlew clean`, as Gradle might download dependencies.
3.  Run:
    ```bash
    ./gradlew main:deploy
    ```
    (Use `gradlew.bat main:deploy` on Windows).
    The JAR will be in `main/build/libs/`.
4.  To automatically copy this JAR to your Mindustry mods folder:
    ```bash
    ./gradlew install
    ```
    You can combine these: `./gradlew main:deploy install`.

### Android Build (Cross-Platform)

This produces a JAR compatible with both Android and PC (e.g., `MyAwesomeMod.jar`).

*   **Using GitHub Actions (Recommended):**
    *   Push your changes to your GitHub repository.
    *   The CI workflow (defined in `.github/workflows/ci.yml`) will automatically build both Desktop and Android JARs.
    *   You can download these from the "Artifacts" section of the completed workflow run. The Android-compatible JAR will be named like `My Awesome Mod (Android).zip` (containing the JAR, name derived from `displayName`).
    *   When you create a GitHub Release, the Android-compatible JAR is automatically uploaded.

*   **Local Android Build (Optional):**
    If you need to build for Android locally:

    1.  **Install Android SDK:**
        *   Download the "**Command line tools only**" package from the [Android Studio page](https://developer.android.com/studio#command-line-tools-only) for your OS.
        *   Extract the ZIP to a directory (e.g., `~/AndroidSDK` on Linux/macOS, `C:\AndroidSDK` on Windows).
        *   Inside the extracted `cmdline-tools` folder, create a new folder named `latest`. Move all contents of `cmdline-tools` (like `bin`, `lib`, etc.) *into* this `latest` folder. The structure should be `AndroidSDK/cmdline-tools/latest/`.
        *   Set the `ANDROID_HOME` (or `ANDROID_SDK_ROOT`) environment variable to the full path of your `AndroidSDK` directory (e.g., `~/AndroidSDK`). Restart your terminal for changes to take effect.
        *   Navigate your terminal to `AndroidSDK/cmdline-tools/latest/bin/`.
        *   Run `sdkmanager --licenses` and accept all licenses by typing 'y' and pressing Enter for each.
        *   Install the necessary SDK platforms and build tools. The versions are specified in `.github/workflows/ci.yml` (look for the `sdkmanager` command):
            ```bash
            sdkmanager "platforms;android-33" "build-tools;33.0.2"
            ```
    2.  **Build the Mod:**
        *   In your mod's root directory, run:
            ```bash
            ./gradlew main:dex
            ```
        *   The cross-platform JAR will be located in `main/build/libs/`.

## Key Features Deep Dive

*   **Annotations & Code Generation (`annotations` module):** This is the heart of the template's power. Explore `Annotations.java` (likely in `annotations/src/yourmodpackage/annotations/`) to see available annotations. Processors in `annotations/src/yourmodpackage/annotations/processors/` handle the generation. For example, `@EntityDef` and `@EntityComponent` simplify entity creation, while `@LoadRegs` automates `TextureRegion` field generation in a `Regions` class.
*   **Asset Processing (`tools` module):** The `tools:proc` task executes logic in `tools/src/yourmodpackage/tools/Processors.java`. You can add custom `Processor` implementations to automate tasks like sprite sheet packing, sound conversion, or custom data generation.
*   **Vanilla Component Fetching (`main:fetchComps`):** Allows you to work with slightly modified versions of Mindustry's own entity components, making it easier to extend or interact with vanilla game mechanics.
*   **Jabel:** Enables you to use modern Java syntax (records, text blocks, var, etc., up to Java 17) while the compiled bytecode remains Java 8 compatible, ensuring your mod runs on Mindustry.

## Notable Gradle Tasks

*   `main:deploy`: Builds the desktop-only JAR.
*   `main:dex`: Builds the Android-compatible (cross-platform) JAR.
*   `install`: Copies the `main:deploy` output to the local Mindustry mods folder.
*   `tools:proc`: Runs the asset processing pipeline (defined in the `tools` module).
*   `main:fetchComps`: Downloads and adapts Mindustry's core entity components into `main/build/fetched/`.
*   `updateBundles`: Synchronizes localization files in `main/assets/bundles/` based on `bundle.properties`. Changes are automatically committed by CI.
*   `clean`: Deletes all `build` directories.
*   `cleanFetched`: Deletes only the fetched vanilla components from `main/build/fetched/`.
*   `tools:rearchive`: If you run `tools:proc` and only assets change, this task can update your existing built JARs (from `main:deploy` and `main:dex`) with the new assets without fully recompiling the `main` module's Java code.

## Adding Dependencies

*   **Mindustry / Arc / Other Mindustry Mods:**
    Always use `compileOnly` as these are provided by the game at runtime and should not be bundled.
    ```gradle
    // In main/build.gradle
    dependencies {
        compileOnly "com.github.Anuken.Mindustry:core:$mindustryVersion"
        compileOnly "com.github.Anuken.Arc:arc-core:$arcVersion"
        // Example: compileOnly "com.github.author:other-mod-api:1.2.3"
    }
    ```

*   **External Java Libraries (to be bundled with your mod):**
    Use `implementation`.
    ```gradle
    // In main/build.gradle
    dependencies {
        implementation "com.google.code.gson:gson:2.10.1" // Example for a JSON library
    }
    ```

## License

This mod template is provided as a starting point and does not impose a specific license on your derivative work. You are free to choose the license for your mod.
Many Mindustry mods adopt the [GNU GPL v3 License](https://www.gnu.org/licenses/gpl-3.0.en.html). If you opt for this, ensure you include a `LICENSE` file with the GPLv3 text in your project's root directory. Always be mindful of the licenses of Mindustry itself, Arc, and any other libraries or assets you incorporate.