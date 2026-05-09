@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)

    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    android {
        namespace = "com.doodle.core.database"
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
            baseName = "coreDatabase"
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
            implementation(projects.core.domain)

            implementation(libs.kotlin.serialization.core)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.kotlin.immutableCollections)
            implementation(libs.kotlin.coroutines.core)

            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
        }

        commonTest.dependencies {
            implementation(libs.koin.test)
        }



        androidMain.dependencies {
            implementation(libs.koin.android)

            implementation(libs.kotlin.coroutines.android)
//            databaseModule()
//
//            implementation("androidx.paging:paging-runtime:${Versions.Common.paging}")
//            implementation("androidx.room:room-paging:${Versions.Common.roomPaging}")
        }
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}