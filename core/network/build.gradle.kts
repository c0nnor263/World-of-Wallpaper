@file:Suppress("UnstableApiUsage")

import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)

    alias(libs.plugins.koin.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.buildkonfig)
}
val localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))

buildkonfig {
    packageName = "com.doodle.core.network"

    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "PIXABAY_API_KEY", localProperties.getProperty("PIXABAY_API_KEY"))
    }
}

kotlin {
    android {
        namespace = "com.doodle.core.network"
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
            baseName = "coreDomain"
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

            implementation(libs.kotlin.serialization.core)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
            implementation(libs.kotlin.immutableCollections)
            implementation(libs.kotlin.coroutines.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        commonTest.dependencies {
            implementation(libs.koin.test)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.kotlin.coroutines.android)

            implementation(libs.ktor.client.android)
//            networkModule()
//
//            implementation("androidx.paging:paging-runtime:${Versions.Common.paging}")
//            implementation("androidx.paging:paging-compose:${Versions.Common.paging}")
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}