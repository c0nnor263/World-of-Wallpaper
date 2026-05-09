@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)

    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    android {
        namespace = "com.doodle.core.data"
        compileSdk = Versions.Config.compileSdk
        minSdk = Versions.Config.minSdk

        androidResources.enable = false
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
            baseName = "coreData"
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
            implementation(projects.core.database)
            implementation(projects.core.domain)

            implementation(libs.kotlin.serialization.core)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.kotlin.immutableCollections)
            implementation(libs.kotlin.coroutines.core)

            implementation(project.dependencies.platform(libs.paginator.bom))
            implementation(libs.paginator.core)

            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)
        }

        commonTest.dependencies {
            implementation(libs.koin.test)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)

            implementation(libs.kotlin.coroutines.android)

//
//    // Play Billing
//    implementation("com.android.billingclient:billing-ktx:${Versions.PlayServices.billing}")
//
//    // Volley
//    implementation("com.android.volley:volley:${Versions.Network.volley}")
//
//    // Kotlin Immutable Collections
//    libs.android.kotlinImmutableCollections.get(this)
//
//    // Play Review
//    implementation("com.google.android.play:review-ktx:${Versions.PlayServices.playReview}")
//
//    // Paging
//    implementation("androidx.paging:paging-runtime:${Versions.Common.paging}")
//    implementation("androidx.paging:paging-compose:${Versions.Common.paging}")
        }
    }
}
