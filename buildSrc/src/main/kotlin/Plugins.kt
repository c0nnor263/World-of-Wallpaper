import org.gradle.kotlin.dsl.kotlin
import org.gradle.plugin.use.PluginDependenciesSpec



fun PluginType.get(scope: PluginDependenciesSpec) {
    with(scope) {
        when (this@get) {
            PluginType.APPLICATION -> {
                id("com.android.application")
                kotlin("android")
                id("com.google.devtools.ksp")
                id("com.google.gms.google-services")
                id("com.google.firebase.crashlytics")
                id("com.google.firebase.firebase-perf")
                id("com.google.dagger.hilt.android")
                id("androidx.baselineprofile")
            }

            else -> {
                id("com.android.library")
                kotlin("android")
                id("kotlin-parcelize")
                id("com.google.devtools.ksp")
                id("com.google.dagger.hilt.android")
            }
        }
    }
}