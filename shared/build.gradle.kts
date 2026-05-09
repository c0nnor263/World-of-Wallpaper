
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "com.doodle.turboracing3.shared"
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
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            export(projects.core.advertising)
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
            api(projects.core.advertising)
            implementation(projects.core.data)
            implementation(projects.core.database)
            implementation(projects.core.domain)
            implementation(projects.core.navigation)
            implementation(projects.core.network)
            implementation(projects.core.ui)

            implementation(projects.feature.splash)
            implementation(projects.feature.home)
            implementation(projects.feature.search)
            implementation(projects.feature.picturedetails)
            implementation(projects.feature.favorites)

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


            implementation(libs.kotlin.immutableCollections)
            implementation(libs.kotlin.serialization.core)
            implementation(libs.kotlin.coroutines.core)

            implementation(libs.androidx.navigationCompose)
            implementation(libs.coil)
            implementation(libs.coil.networkKtor3)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.kotlin.coroutines.android)
            implementation(libs.playServices.ads)
        }
    }
}

dependencies{
    androidRuntimeClasspath(libs.compose.uiTooling)
}