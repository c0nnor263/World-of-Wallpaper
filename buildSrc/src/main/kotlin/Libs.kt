//import org.gradle.api.artifacts.dsl.DependencyHandler
//import org.gradle.kotlin.dsl.project
//
//object libs {
//
//    object firebase {
//        val bom =
//            Dependency("com.google.firebase:firebase-bom", Versions.Firebase.bom, Type.PLATFORM)
//        val crashlyticsKtx = Dependency("com.google.firebase:firebase-crashlytics-ktx")
//        val analyticsKtx = Dependency("com.google.firebase:firebase-analytics-ktx")
//        val perfKtx = Dependency("com.google.firebase:firebase-perf-ktx")
//    }
//
//    object storage {
//        val room = Dependency("androidx.room:room-runtime", Versions.Storage.room)
//        val roomCompiler =
//            Dependency("androidx.room:room-compiler", Versions.Storage.room, Type.KSP)
//        val roomKtx = Dependency("androidx.room:room-ktx", Versions.Storage.room)
//        val datastorePreferences =
//            Dependency("androidx.datastore:datastore-preferences", Versions.Storage.dataStore)
//    }
//
//    object tooling {
//        val junit = Dependency("junit:junit", Versions.Tooling.junit, Type.TEST)
//        val androidJunit =
//            Dependency(
//                "androidx.test.ext:junit",
//                Versions.Tooling.junitKtx,
//                Type.ANDROID_TEST
//            )
//        val espressoCore = Dependency(
//            "androidx.test.espresso:espresso-core",
//            Versions.Tooling.androidEspressoCore,
//            Type.ANDROID_TEST
//        )
//        val composeJunit4 = Dependency(
//            "androidx.compose.ui:ui-test-junit4",
//            version = Versions.Tooling.composeJunit,
//            type = Type.ANDROID_TEST
//        )
//        val composeUiTestManifest = Dependency(
//            "androidx.compose.ui:ui-test-manifest",
//            type = Type.DEBUG
//        )
//        val daggerHiltAndroidTesting = Dependency(
//            "com.google.dagger:hilt-android-testing",
//            Versions.Common.hilt,
//            Type.ANDROID_TEST
//        )
//
//        val daggerHiltAndroidCompiler = Dependency(
//            "com.google.dagger:hilt-android-compiler",
//            Versions.Common.hilt,
//            Type.KSP
//        )
//
//        val runner = Dependency(
//            "androidx.test:runner",
//            Versions.Tooling.runner,
//            Type.ANDROID_TEST
//        )
//        val rules = Dependency(
//            "androidx.test:rules",
//            Versions.Tooling.rules,
//            Type.ANDROID_TEST
//        )
//        val testJunitKtx = Dependency(
//            "androidx.test.ext:junit-ktx",
//            Versions.Tooling.junitKtx,
//            Type.TEST
//        )
//        val coreKtx = Dependency("androidx.test:core-ktx", Versions.Tooling.coreKtx, Type.TEST)
//        val robolectric =
//            Dependency("org.robolectric:robolectric", Versions.Tooling.robolectric, Type.TEST)
//        val mockito = Dependency("org.mockito:mockito-core", Versions.Tooling.mockito, Type.TEST)
//        val mockitoKotlin = Dependency(
//            "org.mockito.kotlin:mockito-kotlin",
//            Versions.Tooling.mockitoKotlin,
//            Type.TEST
//        )
//        val kotlinxCoroutinesTest = Dependency(
//            "org.jetbrains.kotlinx:kotlinx-coroutines-test",
//            Versions.Android.coroutines,
//            Type.TEST
//        )
//    }
//
//    object compose {
//
//        val bom = Dependency("androidx.compose:compose-bom", Versions.Compose.bom, Type.PLATFORM)
//        val ui = Dependency("androidx.compose.ui:ui")
//        val uiGraphics = Dependency("androidx.compose.ui:ui-graphics")
//        val uiToolingPreview = Dependency("androidx.compose.ui:ui-tooling-preview")
//        val material3 =
//            Dependency("androidx.compose.material3:material3")
//        val materialIconsExtended =
//            Dependency("androidx.compose.material:material-icons-extended")
//        val uiTooling = Dependency("androidx.compose.ui:ui-tooling", type = Type.DEBUG)
//        val uiTestManifest = Dependency("androidx.compose.ui:ui-test-manifest", type = Type.DEBUG)
//        val navigationCompose =
//            Dependency("androidx.navigation:navigation-compose", Versions.Compose.navigation)
//        val hiltNavigationCompose =
//            Dependency("androidx.hilt:hilt-navigation-compose", Versions.Compose.hiltNavigation)
//        val constraintLayoutCompose = Dependency(
//            "androidx.constraintlayout:constraintlayout-compose",
//            Versions.Compose.constraintLayout
//        )
//        val coil = Dependency(
//            "io.coil-kt:coil-compose",
//            Versions.Compose.coil
//        )
//
//        val composeUtil = Dependency(
//            "androidx.compose.ui:ui-util",
//            Versions.Compose.composeUtil
//        )
//
//        val all = listOf(
//            bom,
//            ui,
//            uiGraphics,
//            uiToolingPreview,
//            material3,
//            materialIconsExtended,
//            uiTooling,
//            uiTestManifest,
//            navigationCompose,
//            hiltNavigationCompose,
//            constraintLayoutCompose,
//            composeUtil
//        )
//    }
//
//    object android {
//        val kotlinImmutableCollections =
//            Dependency(
//                "org.jetbrains.kotlinx:kotlinx-collections-immutable",
//                Versions.Android.kotlinImmutableCollections
//            )
//    }
//
//    object lifecycle {
//        val runtimeKtx =
//            Dependency("androidx.lifecycle:lifecycle-runtime-ktx", Versions.Android.lifecycle)
//        val viewmodelKtx =
//            Dependency("androidx.lifecycle:lifecycle-viewmodel-ktx", Versions.Android.lifecycle)
//    }
//
//    object hilt {
//        val hiltAndroid = Dependency("com.google.dagger:hilt-android", Versions.Common.hilt)
//        val hiltAndroidCompiler =
//            Dependency("com.google.dagger:hilt-android-compiler", Versions.Common.hilt, Type.KSP)
//    }
//
//    object coroutines {
//        val core =
//            Dependency("org.jetbrains.kotlinx:kotlinx-coroutines-core", Versions.Android.coroutines)
//        val android =
//            Dependency(
//                "org.jetbrains.kotlinx:kotlinx-coroutines-android",
//                Versions.Android.coroutines
//            )
//    }
//
//    object playServices {
//        val billingKtx =
//            Dependency("com.android.billingclient:billing-ktx", Versions.PlayServices.billing)
//        val ads =
//            Dependency("com.google.android.gms:play-services-ads", Versions.PlayServices.ads)
//        val integrity =
//            Dependency("com.google.android.play:integrity", Versions.PlayServices.integrity)
//    }
//
//    object startup {
//        val profileinstaller =
//            Dependency(
//                "androidx.profileinstaller:profileinstaller",
//                Versions.Startup.profileinstaller
//            )
//        val startupRuntime =
//            Dependency("androidx.startup:startup-runtime", Versions.Startup.startupRuntime)
//    }
//
//    object network {
//        val retrofit = Dependency("com.squareup.retrofit2:retrofit", Versions.Network.retrofit)
//        val retrofitConverterGson =
//            Dependency("com.squareup.retrofit2:converter-gson", Versions.Network.converterGson)
//        val httpLoggingInterceptor = Dependency(
//            "com.squareup.okhttp3:logging-interceptor",
//            Versions.Network.httpLoggingInterceptor
//        )
//        val gson = Dependency("", Versions.Network.gson)
//    }
//}
//
//fun DependencyHandler.composeCore() {
//    hilt()
//    test()
//    libs.android.coreKtx.get(this)
//    libs.android.activity.get(this)
//    libs.android.kotlinImmutableCollections.get(this)
//    libs.lifecycle.runtimeComposeKtx.get(this)
//    getAll(this, libs.compose.all)
//}
//
//fun DependencyHandler.test() {
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
//}
//
//fun DependencyHandler.firebase() {
//    libs.firebase.bom.get(this)
//    libs.firebase.crashlyticsKtx.get(this)
//    libs.firebase.analyticsKtx.get(this)
//    libs.firebase.perfKtx.get(this)
//}
//
//fun DependencyHandler.lifecycle() {
//    libs.lifecycle.runtimeKtx.get(this)
//    libs.lifecycle.viewmodelKtx.get(this)
//    libs.lifecycle.viewmodelCompose.get(this)
//}
//
//fun DependencyHandler.room() {
//    libs.storage.room.get(this)
//    libs.storage.roomCompiler.get(this)
//    libs.storage.roomKtx.get(this)
//}
//
//fun DependencyHandler.fullStorage() {
//    room()
//    libs.storage.datastorePreferences.get(this)
//}
//
//fun DependencyHandler.coroutines() {
//    libs.coroutines.core.get(this)
//    libs.coroutines.android.get(this)
//}
//
//fun DependencyHandler.hilt() {
//    libs.hilt.hiltAndroid.get(this)
//    libs.hilt.hiltAndroidCompiler.get(this)
//}
//
//fun DependencyHandler.coreData() {
//    hilt()
//    fullStorage()
//    libs.playServices.ads.get(this)
//}
//
//fun DependencyHandler.appModule() {
//    add("baselineProfile", project(":baselineprofile"))
//    composeCore()
//    firebase()
//    lifecycle()
//    coroutines()
//    libs.startup.profileinstaller.get(this)
//    libs.startup.startupRuntime.get(this)
//    libs.playServices.billingKtx.get(this)
//    libs.playServices.ads.get(this)
//    libs.playServices.integrity.get(this)
//}
//
//fun DependencyHandler.databaseModule() {
//    fullStorage()
//    hilt()
//
//    libs.playServices.ads.get(this)
//}
//
//fun DependencyHandler.networkModule() {
//    network()
//    hilt()
//}
//
//fun DependencyHandler.network() {
//    libs.network.retrofit.get(this)
//    libs.network.retrofitConverterGson.get(this)
//    libs.network.httpLoggingInterceptor.get(this)
//    libs.network.gson.get(this)
//}
//
//fun DependencyHandler.testingModule() {
//    libs.tooling.junit.get(this, Type.DEFAULT)
//    libs.tooling.androidJunit.get(this, Type.DEFAULT)
//    libs.tooling.espressoCore.get(this, Type.DEFAULT)
//    libs.tooling.composeUiTestManifest.get(this, Type.DEFAULT)
//    libs.tooling.composeJunit4.get(this, Type.DEFAULT)
//    libs.tooling.daggerHiltAndroidTesting.get(this, Type.DEFAULT)
//    libs.tooling.daggerHiltAndroidCompiler.get(this, Type.DEFAULT)
//    libs.tooling.runner.get(this, Type.DEFAULT)
//    libs.tooling.rules.get(this, Type.DEFAULT)
//    libs.tooling.testJunitKtx.get(this, Type.DEFAULT)
//    libs.tooling.coreKtx.get(this, Type.DEFAULT)
//    libs.tooling.robolectric.get(this, Type.DEFAULT)
//    libs.tooling.mockito.get(this, Type.DEFAULT)
//    libs.tooling.mockitoKotlin.get(this, Type.DEFAULT)
//    libs.tooling.kotlinxCoroutinesTest.get(this, Type.DEFAULT)
//}
