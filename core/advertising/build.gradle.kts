@file:Suppress("UnstableApiUsage")

import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.buildkonfig)
}

val admobDataPropertiesFile: File = rootProject.file("admobdata.properties")
val admobDataProperties = Properties().apply {
    load(FileInputStream(admobDataPropertiesFile))
}

buildkonfig {
    packageName = "com.doodle.core.advertising"
    exposeObjectWithName = "CoreAdvertisingBuildKonfig"

    defaultConfigs {
        buildConfigField(
            FieldSpec.Type.STRING,
            "application_id",
            admobDataProperties.getProperty("application_id")
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "banner_ad_unit_id",
            admobDataProperties.getProperty("banner_ad_unit_id")
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "app_open_ad_unit_id",
            admobDataProperties.getProperty("app_open_ad_unit_id")
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "native_ad_unit_id",
            admobDataProperties.getProperty("native_ad_unit_id")
        )
        buildConfigField(
            FieldSpec.Type.STRING,
            "rewarded_ad_unit_id",
            admobDataProperties.getProperty("rewarded_ad_unit_id")
        )
    }
}

kotlin {
    android {
        namespace = "com.doodle.core.advertising"
        compileSdk = Versions.Config.compileSdk
        minSdk = Versions.Config.minSdk

        androidResources { enable = true }
        packaging.resources.excludes.add(
            Versions.Compose.exclude,
        )


        withJava() // enable java compilation support
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        optimization.consumerKeepRules.apply {
            publish = true
            file("consumer-rules.pro")
        }
    }

    listOf(
        iosArm64(), iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "coreAdvertising"
        }
    }

    sourceSets {
        getByName("androidHostTest") {
            dependencies {}
        }

        getByName("androidDeviceTest") {
            dependencies {}
        }

        commonMain.dependencies {
            implementation(projects.core.data)
            implementation(projects.core.domain)
            implementation(projects.core.navigation)
            implementation(projects.core.ui)

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.annotations)

            implementation(libs.kotlin.serialization.core)
            implementation(libs.kotlin.coroutines.core)
            implementation(libs.androidx.navigationCompose)

            implementation(libs.coil)
            implementation(libs.coil.networkKtor3)
        }

        commonTest.dependencies {
            implementation(libs.koin.test)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.androidx.constraintLayout)
            implementation(libs.androidx.cardView)

            implementation(libs.kotlin.coroutines.android)
//            implementation("androidx.appcompat:appcompat:${Versions.Android.appCompat}")
//            coreData()
//            composeCore()
//
            implementation(libs.playServices.ads)
//            libs.compose.coil.get(this)
        }
    }
}