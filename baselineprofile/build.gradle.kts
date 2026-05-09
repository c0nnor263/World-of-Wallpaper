@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    id("com.android.test")
    id("org.jetbrains.kotlin.android")
    id("androidx.baselineprofile")
}

android {
    namespace = "com.doodle.baselineprofile"
    compileSdk = Versions.Config.compileSdk

    compileOptions {
        sourceCompatibility = Versions.Config.sourceCompatibility
        targetCompatibility = Versions.Config.targetCompatibility
    }

    kotlinOptions {
        jvmTarget = Versions.Config.jvmTarget
    }

    defaultConfig {
        minSdk = Versions.Config.minSdk
        targetSdk = Versions.Config.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    }

    targetProjectPath = ":app"

    testOptions.managedDevices.devices {
        create<ManagedVirtualDevice>("pixel6Api34") {
            device = "Pixel 6"
            apiLevel = 34
            systemImageSource = "google"
        }
    }
}
androidComponents {
    onVariants(selector().withBuildType("release")) {
        // Exclude AndroidX version files
        it.packaging.resources.excludes.add("META-INF/*.version")
    }
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
//    managedDevices += "pixel6Api34"
    useConnectedDevices = true
}

dependencies {
    implementation("androidx.test.ext:junit:${Versions.Tooling.junitKtx}")
    implementation("androidx.test.espresso:espresso-core:${Versions.Tooling.androidEspressoCore}")
    implementation("androidx.test.uiautomator:uiautomator:${Versions.Tooling.uiautomator}")
    implementation(
        "androidx.benchmark:benchmark-macro-junit4:${Versions.Tooling.benchmarkMacroJunit4}"
    )
}
