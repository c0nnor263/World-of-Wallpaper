@file:Suppress("UnstableApiUsage")

import java.io.FileInputStream
import java.util.Properties

plugins {
    PluginType.LIBRARY.get(this)
}

val gradleProperties = Properties()
gradleProperties.load(FileInputStream(rootProject.file("gradle.properties")))
android {
    namespace = "com.doodle.core.billing"
    compileSdk = Versions.Config.compileSdk

    defaultConfig {
        minSdk = Versions.Config.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "verifyPurchases",
            "\"${gradleProperties.getProperty("verifyPurchases")}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = Versions.Config.sourceCompatibility
        targetCompatibility = Versions.Config.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = Versions.Config.jvmTarget
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.compiler
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += Versions.Compose.exclude
        }
    }
}

dependencies {
    implementation(project(":core:database"))
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    composeCore()
    implementation("com.android.billingclient:billing-ktx:${Versions.PlayServices.billing}")
    implementation("com.android.volley:volley:${Versions.Android.volley}")
    coreData()

    implementation("com.google.android.play:review-ktx:${Versions.PlayServices.playReview}")
}

