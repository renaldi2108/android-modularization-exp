pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AndroidBase"
include(":app")
include(":baselineprofile")
include(":core:common")
include(":core:utils")
include(":core:network")
include(":core:datastore")
include(":core:shared:designsystem")
include(":core:shared:ui")
include(":feature:auth:domain")
include(":feature:auth:data")
include(":feature:auth:presentation")
include(":feature:products:domain")
include(":feature:products:data")
include(":feature:products:presentation")
include(":feature:users:domain")
include(":feature:users:data")
include(":feature:users:presentation")
include(":feature:posts:domain")
include(":feature:posts:data")
include(":feature:posts:presentation")
include(":feature:todos:domain")
include(":feature:todos:data")
include(":feature:todos:presentation")
include(":feature:quotes:domain")
include(":feature:quotes:data")
include(":feature:quotes:presentation")
include(":feature:carts:domain")
include(":feature:carts:data")
include(":feature:carts:presentation")
include(":feature:recipes:domain")
include(":feature:recipes:data")
include(":feature:recipes:presentation")
include(":feature:comments:domain")
include(":feature:comments:data")
include(":feature:comments:presentation")
