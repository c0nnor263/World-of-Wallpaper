import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)


//    alias(libs.plugins.ksp)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.performance)
//    id("androidx.baselineprofile")
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    load(FileInputStream(keystorePropertiesFile))
}

kotlin {
    target {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

android {
    namespace = "com.doodle.turboracing3"
    compileSdk = Versions.Config.compileSdk

    defaultConfig {
        applicationId = "com.doodle.turboracing3"
        minSdk = Versions.Config.minSdk
        targetSdk = Versions.Config.targetSdk
        versionCode = 1
        versionName = "1.0"
    }

    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }

    packaging {
        resources {
            excludes += Versions.Compose.exclude
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = Versions.Config.sourceCompatibility
        targetCompatibility = Versions.Config.targetCompatibility
    }
}

dependencies {
    implementation(projects.shared)
//    implementation(project(":core:billing"))
    implementation(projects.core.advertising)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.ui)
//    add("baselineProfile", project(":baselineprofile"))


    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.uiToolingPreview)

    debugImplementation(libs.compose.uiTooling)
//    implementation(platform("androidx.compose:compose-bom:${Versions.Compose.bom}"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
//    implementation("androidx.compose.material:material-icons-extended")
//    debugImplementation("androidx.compose.ui:ui-tooling")
//    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.navigation:navigation-compose:${Versions.Compose.navigation}")
//    implementation("androidx.constraintlayout:constraintlayout-compose:${Versions.Compose.constraintLayout}")
//    implementation("androidx.compose.ui:ui-util:${Versions.Compose.composeUtil}")


//    libs.tooling.junit.get(this)
//    libs.tooling.androidJunit.get(this)
//    libs.tooling.espressoCore.get(this)
//    libs.tooling.composeUiTestManifest.get(this)
//    libs.tooling.composeJunit4.get(this)
//    libs.tooling.daggerHiltAndroidTesting.get(this)
//    libs.tooling.daggerHiltAndroidCompiler.get(this)
//    libs.tooling.runner.get(this)
//    libs.tooling.rules.get(this)
//    libs.tooling.testJunitKtx.get(this)
//    libs.tooling.coreKtx.get(this)
//    libs.tooling.robolectric.get(this)
//    libs.tooling.mockito.get(this)
//    libs.tooling.mockitoKotlin.get(this)
//    libs.tooling.kotlinxCoroutinesTest.get(this)
//
//
//    libs.android.coreKtx.get(this)
//    libs.android.activity.get(this)
//    libs.android.kotlinImmutableCollections.get(this)
//    libs.lifecycle.runtimeComposeKtx.get(this)
//
//

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlyticsKtx)
    implementation(libs.firebase.analyticsKtx)
    implementation(libs.firebase.perfKtx)

//
//
//    libs.lifecycle.runtimeKtx.get(this)
//    libs.lifecycle.viewmodelKtx.get(this)
//    libs.lifecycle.viewmodelCompose.get(this)
//
//
//    libs.coroutines.core.get(this)
//    libs.coroutines.android.get(this)
//
//
//    libs.startup.profileinstaller.get(this)
      implementation(libs.androidx.startup)
//    libs.playServices.billingKtx.get(this)
    implementation(libs.playServices.ads)
//    libs.playServices.integrity.get(this)
//
    implementation(libs.gson)

    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.annotations)
}