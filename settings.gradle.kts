rootProject.name = "WorldOfWallpapers"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":shared")
include(":androidApp")
//include(":baselineprofile")

include(":core:advertising")
include(":core:data")
include(":core:database")
include(":core:domain")
include(":core:navigation")
include(":core:network")
include(":core:ui")
//include(":core:billing")


//include(":core:testing")
include(":feature:splash")
include(":feature:home")
include(":feature:search")
include(":feature:picturedetails")
include(":feature:favorites")


