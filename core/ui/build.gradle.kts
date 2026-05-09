@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.koin.compiler)
}

compose.resources {
    publicResClass = true
}

kotlin {
    android {
        namespace = "com.doodle.core.ui"
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
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "coreUi"
        }
    }

    sourceSets {
        getByName("androidHostTest") {
            dependencies {
            }
        }

        getByName("androidDeviceTest") {
            dependencies {
            }
        }

        commonMain.dependencies {
            implementation(projects.core.data)
            implementation(projects.core.domain)

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

            implementation(libs.kotlin.coroutines.android)


//    coreData()
//    composeCore()
//
//    implementation("io.coil-kt:coil-compose:${Versions.Compose.coil}")
//    implementation("androidx.paging:paging-runtime:${Versions.Common.paging}")
//    implementation("androidx.paging:paging-compose:${Versions.Common.paging}")
//    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
        }
    }
}
